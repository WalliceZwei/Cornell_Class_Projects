import re, os, subprocess, sys

if __name__ == "__main__":
    test_file_path = sys.argv[-1]
    try:
        c_result = subprocess.run(['make', '-C', '.'], 
                                  timeout=20, check=True, capture_output=True, text=True)
        print(f"Compiled correctly with the output:\n{c_result.stdout}")
    except Exception as e:
        print(f"Fails to compile the runner with the exception: {e}")
        exit(1)

    with open(test_file_path, "r") as f:
        content = f.read()
    sections = re.split(r'(?=CMDS:)', content)[1:]

    num_tests = 0
    for section in sections:
        lines = section.strip().split('\n')
        test_asm = []
        cmd_args = lines[0].replace("CMDS:", "").split()

        for line in lines[1:]:
            line = line.strip()
            if line.startswith("OUTS:"):
                break
            test_asm.append(line)

        
            with open(f"./test{num_tests:02d}.s", "w") as f:
                f.write("\n".join(test_asm))

        try:
            subprocess.run(['as', f"./test{num_tests:02d}.s", '-o', './tmp.o'], capture_output=True, check=True, text=True, timeout=20)
            subprocess.run(['objcopy', './tmp.o', '-O', 'binary', './test.bin'], capture_output=True, check=True, text=True, timeout=20)
        except:
            print(f"Failed to generate binary file for test{num_tests:02d}. Check correctness of test.")
        else:
            test_result = subprocess.run(['qemu', './runner'] + cmd_args, stdin=open('./test.bin', 'r'), capture_output=True, text=True, timeout=20)   
            if test_result.returncode:
                print(f"Failed to run test{num_tests:02d} with return code {test_result.returncode}:\nstdout: {test_result.stdout}\nstderr: {test_result.stderr}")
            else:
                print(f"test{num_tests:02d} outputs:\n{test_result.stdout}")
            
                
        num_tests += 1

    try:
        subprocess.run(['make', 'clean'], capture_output=True, timeout=10)
        subprocess.run(['make', 'coverage'], capture_output=True, timeout=10)
        subprocess.run(['rm', './test.bin'], capture_output=True, timeout=10)
    except Exception as e:
        print(f"Failed to clean compilation files: {e}")
        
