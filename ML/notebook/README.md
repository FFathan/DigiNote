# This folder contain notebook to create and train the model

The notebook was trained on Google Colab with GCP VM. Images samples used are 30000 training images and 3000 validation images (only 10% of total datasets, because of limited resource). Training process took 4 hours to complete. The model is trained with 60 epochs and batch size of 128. The model is trained end-to-end with CTC loss function. The model is trained with Adam optimizer with learning rate of 0.001.
