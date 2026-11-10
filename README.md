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


### AI:

Statistics file, especially plotting part, was made with an AI agent (Copilot GPT-6 Luna), because I do not want to waste a lot of time relearning pyplot.
In addition to that, one function in the *SorterExperiment.java* was written completely by AI, only refined by me, as well as IO of writing down into csv files.

To find more uses of AI, search for the term (Ctrl+F -> "AI"(case sensitive search))