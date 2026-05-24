import os
import matplotlib.pyplot as plt
import pandas as pd
import numpy as np

methods = ["insert", "peek", "poll", "increase_priority"]
trial_sizes = ["100k", "500k", "1m"]
trials = [1, 2, 3]
colors = {
    '100k': '#6A1B94',
    '500k': 'blue',
    '1m': 'red'
}
directory = "/Users/gabrielcastillo/Downloads/critterworld-rq48-gac232-nvt5-wzw4/src/test/java/a6/performance"

def formatter(num):
    for unit in ['', 'K', 'M', 'B']:
        if abs(num) < 1000:
            return f"{num:.0f}{unit}"
        num /= 1000
    return f"{num:.0f}B"

def create_graph(data, average_times, method, trial_size, y_limit):
    plt.figure(figsize=(10, 6))
    plt.scatter(data["Number of Elements"], average_times, s=2, color=colors[trial_size], zorder=2)
    plt.title(f"Binary Heap {method.capitalize()} ({trial_size}, Average)")
    plt.xlabel("Number of Elements")
    plt.ylabel("Time in Nanoseconds")
    plt.ylim(0, y_limit)
    plt.grid(True, zorder=1)
    plt.gca().get_xaxis().set_major_formatter(plt.FuncFormatter(lambda x, _: formatter(int(x))))
    plt.gca().get_yaxis().set_major_formatter(plt.FuncFormatter(lambda y, _: formatter(int(y))))

    middle_index_start = len(data) // 2 - 250
    middle_index_end = len(data) // 2 + 250
    axins = plt.gca().inset_axes([0.7, 0.15, 0.25, 0.25])
    axins.scatter(data["Number of Elements"].iloc[middle_index_start:middle_index_end],
                  average_times[middle_index_start:middle_index_end],
                  s=2, color=colors[trial_size], zorder=2)
    axins.set_xlim(data["Number of Elements"].iloc[middle_index_start],
                   data["Number of Elements"].iloc[middle_index_end])
    axins.set_ylim(0, y_limit / 20)
    axins.set_xticks([])
    axins.set_yticks([])
    plt.gca().indicate_inset_zoom(axins, edgecolor="black")

    plt.savefig(os.path.join(directory, f"heap_{method}_avg_{trial_size}.png"), dpi=300)
    plt.close()

for method in methods:
    y_limits = {
        "insert": 100000,
        "peek": 1000,
        "poll": 100000,
        "increase_priority": 100000
    }
    for trial_size in trial_sizes:
        average_times = None
        for trial in trials:
            file_path = os.path.join(directory, f"heap_{method}_{trial_size}_{trial}.csv")
            data = pd.read_csv(file_path)
            if average_times is None:
                average_times = data["NanoTime"]
            else:
                average_times += data["NanoTime"]
        average_times /= len(trials)

        create_graph(data, average_times, method, trial_size, y_limits[method])