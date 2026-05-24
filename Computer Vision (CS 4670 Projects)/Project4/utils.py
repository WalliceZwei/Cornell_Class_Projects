import clip
import numpy as np
import os
import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
from torch.utils.data import Dataset, DataLoader
from torchvision import transforms
from torchvision.datasets import ImageFolder
from PIL import Image
import matplotlib.pyplot as plt
from sklearn.manifold import TSNE
from sklearn.metrics import confusion_matrix, classification_report
import seaborn as sns
from tqdm import tqdm
import zipfile

class MiniCUB(Dataset):
    def __init__(self, root_dir, split="train", transform=None):
        self.root_dir = root_dir
        self.split = split
        self.transform = transform
        self.classes = sorted(os.listdir(os.path.join(root_dir, split)))
        self.class_to_idx = {cls: i for i, cls in enumerate(self.classes)}
        self.images = []
        self.labels = []

        for cls in self.classes:
            class_dir = os.path.join(root_dir, split, cls)
            for img_name in os.listdir(class_dir):
                self.images.append(os.path.join(class_dir, img_name))
                self.labels.append(self.class_to_idx[cls])

    def __len__(self):
        return len(self.images)

    def __getitem__(self, idx):
        img_path = self.images[idx]
        image = Image.open(img_path).convert('RGB')
        label = self.labels[idx]

        if self.transform:
            image = self.transform(image)

        return image, label

def plot_training_progress(train_losses, train_accs, test_accs, filename='training_progress'):
    viz_dir = './visualizations'
    plt.figure(figsize=(12, 5))

    plt.subplot(1, 2, 1)
    plt.plot(train_losses)
    plt.title('Training Loss')
    plt.xlabel('Epoch')
    plt.ylabel('Loss')
    plt.grid(True, alpha=0.3)

    plt.subplot(1, 2, 2)
    plt.plot(train_accs, label='Train', marker='o')
    plt.plot(test_accs, label='Test', marker='s')
    plt.title('Accuracy')
    plt.xlabel('Epoch')
    plt.ylabel('Accuracy (%)')
    plt.legend()
    plt.grid(True, alpha=0.3)

    plt.tight_layout()
    plt.savefig(f'{viz_dir}/{filename}.png')
    # plt.show()
    plt.close()

def plot_confusion_matrix(all_preds, all_labels, class_names, filename='confusion_matrix'):
    viz_dir = './visualizations'
    cm = confusion_matrix(all_labels, all_preds)
    plt.figure(figsize=(10, 8))
    sns.heatmap(cm, annot=True, fmt='d', cmap='Blues', xticklabels=class_names, yticklabels=class_names)
    plt.xlabel('Predicted')
    plt.ylabel('True')
    plt.title('Confusion Matrix')
    plt.tight_layout()
    plt.savefig(f'{viz_dir}/{filename}.png')
    # plt.show()
    plt.close()

def plot_tsne(features, labels, class_names, filename='tsne_visualization'):
    viz_dir = './visualizations'
    tsne = TSNE(n_components=2, random_state=42)
    embeddings = tsne.fit_transform(features)

    plt.figure(figsize=(10, 8))
    for i, cls in enumerate(class_names):
        idx = np.where(np.array(labels) == i)[0]
        plt.scatter(embeddings[idx, 0], embeddings[idx, 1], label=cls, alpha=0.7)

    plt.legend()
    plt.title('t-SNE visualization of image features')
    plt.tight_layout()
    plt.savefig(f'{viz_dir}/{filename}.png')
    # plt.show()
    plt.close()

def visualize_samples(dataset, class_names):
    viz_dir = './visualizations'
    samples = {}  # dictionary to hold one sample per class
    # Loop over the dataset until we have one image for each class
    for idx in range(len(dataset)):
        img, label = dataset[idx]
        if label not in samples:
            samples[label] = img
        if len(samples) == len(class_names):  # assume class_names is a list of class names
            break

    num_samples = len(samples)
    plt.figure(figsize=(15, 8))
    # Sort the samples by class label for consistent ordering
    for i, (label, img) in enumerate(sorted(samples.items())):
        # Convert from tensor to numpy for visualization
        img_np = img.permute(1, 2, 0).numpy()
        # Normalize the image
        img_np = (img_np - img_np.min()) / (img_np.max() - img_np.min())

        plt.subplot(2, num_samples // 2, i + 1)
        plt.imshow(img_np)
        plt.title(f"Class: {class_names[label]}")
        plt.axis('off')

    plt.tight_layout()
    plt.show()
    plt.close()

def run_text_prompts(CLIPWithTextPrompts, clip_model, device, test_loader, class_names, evaluate, custom_prompts=None):
    print("\n======= Running CLIP with Natural Text Prompts =======")
    # Set default prompts if none are provided
    if custom_prompts is None:
        custom_prompts = [f"a photo of a {class_name}" for class_name in class_names]

    print("Using the following text prompts for classification:")
    for cls, prompt in zip(class_names, custom_prompts):
        print(f"{cls}: '{prompt}'")

    # Create an instance using the provided CLIPWithTextPrompts
    text_model = CLIPWithTextPrompts(custom_prompts, clip_model, device).to(device)

    # Evaluate on the test set using your pre-defined evaluate function
    text_acc, text_preds, text_labels, text_features = evaluate(text_model, test_loader)

    print("\nClassification Report (Natural Text Prompts):")
    print(classification_report(text_labels, text_preds, target_names=class_names))

    # Create visualizations for the text prompt method
    plot_confusion_matrix(text_preds, text_labels, class_names, 'text_confusion_matrix')
    plot_tsne(text_features, text_labels, class_names, 'text_tsne')

    return text_acc, text_preds, text_labels, text_features, custom_prompts

def train_and_eval(
    ft_model,  # external class for the learnable model
    clip_model,                  # pretrained CLIP model provided externally
    device,                      # the device to run the model on (e.g., "cuda" or "cpu")
    num_classes,                 # number of output classes
    train,                       # training function defined in the notebook
    train_loader,                # DataLoader for training data
    test_loader,                 # DataLoader for test data
    opt_params,                  # optimizer parameters
    lr,                          # learning rate for optimizer
    weight_decay,                # weight decay for optimizer
    num_epochs,                  # number of training epochs
    evaluate,                    # evaluation function defined in the notebook
    class_names,                  # list of class names for visualization and reporting
    out
    ):
    print("\n======= Running CLIP with Learnable Embeddings =======")

    # Initialize model
    ft_model = ft_model(clip_model, num_classes).to(device)

    # Optimizer
    params = getattr(ft_model, opt_params)
    if isinstance(params, nn.Linear):
        params = params.parameters()
    elif isinstance(params, nn.Parameter):
        params = [params]
    optimizer = optim.AdamW(params, lr=lr, weight_decay=weight_decay)

    # Learning rate scheduler
    scheduler = optim.lr_scheduler.CosineAnnealingLR(optimizer, T_max=num_epochs)

    # Training loop variables
    train_losses = []
    train_accs = []
    test_accs = []
    best_acc = 0

    # Training loop
    for epoch in range(1, num_epochs + 1):
        train_loss, train_acc = train(ft_model, train_loader, optimizer, epoch)
        train_losses.append(train_loss)
        train_accs.append(train_acc)

        # Evaluate on test set
        test_acc, _, _, _ = evaluate(ft_model, test_loader)
        test_accs.append(test_acc)

        # Update learning rate
        scheduler.step()

        # Save best model if it improves
        if test_acc > best_acc:
            best_acc = test_acc
            print(f"New best accuracy: {best_acc:.2f}%")
            best_model_weights = ft_model.state_dict()
            opt_weights = {k:v for k, v in ft_model.state_dict().items() if opt_params in k}
            torch.save(opt_weights, f"best_{out}_weights.pth")

    # Plot training progress (function defined in your module)
    plot_training_progress(train_losses, train_accs, test_accs, f'{out}_training_progress')

    # Load best model for final evaluation
    ft_model.load_state_dict(best_model_weights)
    # ft_model.load_state_dict(torch.load(f"best_{out}_weights.pth", weights_only=True))

    # Final evaluation and visualizations
    test_acc, preds, labels, features = evaluate(ft_model, test_loader)

    # print("\nClassification Report (Learnable Embeddings):")
    # print(classification_report(labels, preds, target_names=class_names))

    # Create visualizations (functions defined in your module)
    plot_confusion_matrix(preds, labels, class_names, f'{out}_confusion_matrix')
    plot_tsne(features, labels, class_names, f'{out}_tsne')

    return test_acc, preds, labels, features


def compare_methods(learnable_results, text_results, num_classes, class_names):
    """
    Compare the performance of different CLIP methods

    Args:
        learnable_results: Tuple of (acc, preds, labels, features) from learnable embeddings
        text_results: Tuple of (acc, preds, labels, features, best_template) from text prompts
    """
    print("\n======= Comparing Methods =======")

    # Unpack results
    learnable_acc, learnable_preds, learnable_labels, learnable_features = learnable_results
    text_acc, text_preds, text_labels, text_features, best_template = text_results

    # Compare accuracies
    print(f"CLIP with Learnable Embeddings Accuracy: {learnable_acc:.2f}%")
    print(f"CLIP with Natural Text Prompts Accuracy: {text_acc:.2f}%")
    print(f"Best template: '{best_template}'")

    # Plot comparison
    methods = ['Learnable Embeddings', 'Natural Text Prompts']
    accuracies = [learnable_acc, text_acc]

    plt.figure(figsize=(8, 6))
    bars = plt.bar(methods, accuracies, alpha=0.7)
    plt.xlabel('Method')
    plt.ylabel('Accuracy (%)')
    plt.title('Comparison of CLIP Methods')
    plt.ylim(0, 100)
    plt.grid(axis='y', alpha=0.3)

    # Add value labels on top of bars
    for bar in bars:
        height = bar.get_height()
        plt.text(bar.get_x() + bar.get_width()/2., height + 0.5,
                f'{height:.1f}%', ha='center', va='bottom')

    plt.tight_layout()
    plt.savefig('./visualizations/method_comparison.png')
    plt.show()
    plt.close()

    # Compare class-wise performance
    learnable_class_acc = np.zeros(num_classes)
    text_class_acc = np.zeros(num_classes)

    # Calculate class-wise accuracy for learnable embeddings
    for pred, label in zip(learnable_preds, learnable_labels):
        if pred == label:
            learnable_class_acc[label] += 1

    # Calculate class-wise accuracy for text prompts
    for pred, label in zip(text_preds, text_labels):
        if pred == label:
            text_class_acc[label] += 1

    # Count occurrences of each class in test set
    class_counts = np.zeros(num_classes)
    for label in learnable_labels:
        class_counts[label] += 1

    # Convert to percentages
    for i in range(num_classes):
        if class_counts[i] > 0:
            learnable_class_acc[i] = (learnable_class_acc[i] / class_counts[i]) * 100
            text_class_acc[i] = (text_class_acc[i] / class_counts[i]) * 100

    # Plot class-wise comparison
    plt.figure(figsize=(14, 6))
    x = np.arange(len(class_names))
    width = 0.35

    bars1 = plt.bar(x - width/2, learnable_class_acc, width, alpha=0.7, label='Learnable Embeddings')
    bars2 = plt.bar(x + width/2, text_class_acc, width, alpha=0.7, label='Natural Text Prompts')

    plt.xlabel('Class')
    plt.ylabel('Accuracy (%)')
    plt.title('Class-wise Accuracy Comparison')
    plt.xticks(x, class_names, rotation=90)
    plt.legend()
    plt.grid(axis='y', alpha=0.3)
    plt.tight_layout()
    plt.savefig('./visualizations/class_wise_comparison.png')
    plt.show()
    plt.close()

    # Find cases where the methods disagree
    disagreements = []
    for i, (l_pred, t_pred, label) in enumerate(zip(learnable_preds, text_preds, learnable_labels)):
        if l_pred != t_pred:
            disagreements.append({
                'index': i,
                'true_class': class_names[label],
                'learnable_pred': class_names[l_pred],
                'text_pred': class_names[t_pred],
                'learnable_correct': l_pred == label,
                'text_correct': t_pred == label
            })

    # Print some statistics about disagreements
    total_disagreements = len(disagreements)
    learnable_wins = sum(1 for d in disagreements if d['learnable_correct'] and not d['text_correct'])
    text_wins = sum(1 for d in disagreements if d['text_correct'] and not d['learnable_correct'])
    both_wrong = sum(1 for d in disagreements if not d['learnable_correct'] and not d['text_correct'])

    print(f"\nDisagreement Analysis:")
    print(f"Total samples where methods disagree: {total_disagreements}")
    print(f"Learnable Embeddings correct, Text Prompts incorrect: {learnable_wins}")
    print(f"Text Prompts correct, Learnable Embeddings incorrect: {text_wins}")
    print(f"Both methods incorrect: {both_wrong}")

def evaluate_ft(model, test_loader, device, clip_model):
    model.eval()

    correct = 0
    total = 0
    all_preds = []
    all_labels = []
    all_features = []

    with torch.no_grad():
        for images, labels in tqdm(test_loader, desc="Evaluating Linear Classifier Model"):
            images = images.to(device)
            labels = labels.to(device)

            # Forward pass: obtain classifier outputs
            logits = model(images)

            # Manually compute image features from the frozen CLIP image encoder
            features = clip_model.encode_image(images)
            features = features / features.norm(dim=-1, keepdim=True)

            _, predicted = logits.max(1)

            total += labels.size(0)
            correct += predicted.eq(labels).sum().item()

            all_preds.extend(predicted.cpu().numpy())
            all_labels.extend(labels.cpu().numpy())
            all_features.append(features.cpu().numpy())

    accuracy = 100. * correct / total
    all_features = np.concatenate(all_features, axis=0)
    print(f"\nLinear Classifier Model Test Accuracy: {accuracy:.2f}%")
    return accuracy, all_preds, all_labels, all_features

def compare_all_methods(learnable_results, text_results, ft_results, class_names):
    """
    Compare the performance of three CLIP methods:
      1. CLIP with Natural Text Prompts
      2. CLIP with Learnable Embeddings
      3. Fine-Tuned CLIP with a Linear Classifier

    Displays overall accuracy, class-wise accuracy, and a disagreement analysis.
    """
    print("\n======= Comparing All Three Methods =======")

    # Unpack the results.
    learnable_acc, learnable_preds, learnable_labels, learnable_features = learnable_results
    text_acc, text_preds, text_labels, text_features, best_template = text_results
    ft_acc, ft_preds, ft_labels, ft_features = ft_results

    # Overall Accuracy Comparison.
    print("\nOverall Test Accuracies:")
    print(f"  - Natural Text Prompts: {text_acc:.2f}%")
    print(f"  - Learnable Embeddings: {learnable_acc:.2f}%")
    print(f"  - Linear Classifier: {ft_acc:.2f}%")
    print(f"  (Best template used: '{best_template}')\n")

    methods = ['Natural Text Prompts', 'Learnable Embeddings', 'Linear Classifier']
    accuracies = [text_acc, learnable_acc, ft_acc]

    plt.figure(figsize=(8, 6))
    bars = plt.bar(methods, accuracies, alpha=0.7)
    plt.xlabel('Method')
    plt.ylabel('Accuracy (%)')
    plt.title('Overall Accuracy Comparison of CLIP Methods')
    plt.ylim(0, 100)
    plt.grid(axis='y', alpha=0.3)
    for bar in bars:
        height = bar.get_height()
        plt.text(bar.get_x() + bar.get_width() / 2, height + 0.5, f'{height:.1f}%', ha='center', va='bottom')
    plt.tight_layout()
    plt.savefig('./visualizations/overall_comparison.png')
    plt.show()
    plt.close()

    # --- Class-wise Accuracy Comparison ---
    num_classes = len(class_names)
    learnable_class_acc = np.zeros(num_classes)
    text_class_acc = np.zeros(num_classes)
    ft_class_acc = np.zeros(num_classes)
    class_counts = np.zeros(num_classes)

    for label in learnable_labels:
        class_counts[label] += 1

    def compute_class_acc(preds, labels, acc_array):
        for pred, true in zip(preds, labels):
            if pred == true:
                acc_array[true] += 1

    compute_class_acc(learnable_preds, learnable_labels, learnable_class_acc)
    compute_class_acc(text_preds, text_labels, text_class_acc)
    compute_class_acc(ft_preds, ft_labels, ft_class_acc)

    # Convert counts to percentages
    for i in range(num_classes):
        if class_counts[i] > 0:
            learnable_class_acc[i] = (learnable_class_acc[i] / class_counts[i]) * 100
            text_class_acc[i] = (text_class_acc[i] / class_counts[i]) * 100
            ft_class_acc[i] = (ft_class_acc[i] / class_counts[i]) * 100

    # Plot grouped bar chart for class-wise accuracy
    plt.figure(figsize=(14, 6))
    x = np.arange(num_classes)
    width = 0.25

    plt.bar(x, text_class_acc, width, label='Natural Text Prompts', alpha=0.7)
    plt.bar(x - width, learnable_class_acc, width, label='Learnable Embeddings', alpha=0.7)
    plt.bar(x + width, ft_class_acc, width, label='Linear Classifier', alpha=0.7)

    plt.xlabel('Class')
    plt.ylabel('Accuracy (%)')
    plt.title('Class-wise Accuracy Comparison')
    plt.xticks(x, class_names, rotation=90)
    plt.legend()
    plt.grid(axis='y', alpha=0.3)
    plt.tight_layout()
    plt.savefig('./visualizations/class_wise_comparison_all.png')
    plt.show()
    plt.close()

def analyze_disagreements(preds1, preds2, true_labels, method1, method2, class_names):
    disagreements = []
    for i, (p1, p2, label) in enumerate(zip(preds1, preds2, true_labels)):
        if p1 != p2:
            disagreements.append({
                'index': i,
                'true_class': class_names[label],
                method1: p1 == label,
                method2: p2 == label
            })
    total_disagreements = len(disagreements)
    m1_correct = sum(1 for d in disagreements if d[method1] and not d[method2])
    m2_correct = sum(1 for d in disagreements if d[method2] and not d[method1])
    both_wrong = sum(1 for d in disagreements if not d[method1] and not d[method2])
    return total_disagreements, m1_correct, m2_correct, both_wrong

    pairs = [
        (learnable_preds, text_preds, "Learnable", "Text"),
        (learnable_preds, ft_preds, "Learnable", "FineTuned"),
        (text_preds, ft_preds, "Text", "Linear Classifier")
        ]

    print("\nDisagreement Analysis:")
    for preds_a, preds_b, name_a, name_b in pairs:
        total_d, a_wins, b_wins, both_wrong = analyze_disagreements(preds_a, preds_b, learnable_labels, name_a, name_b)
        print(f"  {name_a} vs. {name_b}:")
        print(f"     Total disagreements: {total_d}")
        print(f"     {name_a} correct, {name_b} incorrect: {a_wins}")
        print(f"     {name_b} correct, {name_a} incorrect: {b_wins}")
        print(f"     Both methods wrong: {both_wrong}\n")