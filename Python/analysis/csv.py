import os

def launch_analysis(data_file):
    directory = os.path.dirname(os.path.dirname(__file__))
    path_to_file = os.path.join(directory,"data",data_file)

    with open(path_to_file, "r") as f:
        preview = f.readline()

    print("Yeah! Readed. Preview:{}".format(preview))

if __name__ == "__main__":
    launch_analysis('current_mps.csv')
