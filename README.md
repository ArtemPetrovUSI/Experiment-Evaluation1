# Startup

(The Jupyter Notebook should already be  pre-rendered)

### To compile:
from Experiment-Evaluation1/exp01 use:

*javac -d . Sorter.java  BubbleSortWhileNeeded.java BubbleSortUntilNoChange.java BubbleSortPassPerItem.java SorterExperiment.java*

### To run the experiments:

*java exp01.SorterExperiment*

The program will 'vomit' .csv files into the folder. It is ok.

### To get the statistics:

Start a jupyter *statistics.ipynb*, choose any kernel with numpy, matplotlib, and pandas, run all cells.

You will get all the plots you need, containig runtimes and standard deviations.


# Overall

The programme is configurable, but must be recompiled, for changes to take effect. You can change retries, but 30 seems fine, you can change the nearly sorted proportions, you can change the seed, as well as sizes of arrays and lengths of strings, how you desire. All of this can help, but overall the experiment is set, so do not expect a big change, the proportios for different tests will remain about the same.