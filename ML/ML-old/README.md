# [NOT USED] The model in this folder is not used in the final project.

## This folder contain trained model with CNN, RNN, LSTM for handwritten recognition in character level.

We tried the first approach to train model in character level, but struggled to feed the data (which is in paragraph form) to the model. The decoding process is too difficult to implement. So we decided to use the second approach, which is to train the model in word level instead and use CTC decoder to decode the output of the model as used in the ML-Final folder.
