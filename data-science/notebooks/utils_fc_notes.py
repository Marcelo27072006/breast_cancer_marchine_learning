import pandas as pd
import matplotlib.pyplot as plt

from sklearn.metrics import (
    accuracy_score,
    f1_score,
    recall_score,
    precision_score,
    roc_auc_score,
    confusion_matrix,
    classification_report
)

def avaliar_modelo(y_true, y_pred):
    resultados = {
        "Acurácia": accuracy_score(y_true, y_pred),
        "Precisão": precision_score(y_true, y_pred, average="binary", zero_division=0),
        "Recall": recall_score(y_true, y_pred, average="binary", zero_division=0),
        "F1-score": f1_score(y_true, y_pred, average="binary", zero_division=0),
        "AUC-ROC": roc_auc_score(y_true, y_pred)
    }
    return pd.DataFrame(resultados, index=["Valor"]).T


def plt_matriz_confusao(y_true, y_pred, nome_modelo='Modelo', salvar=False, caminho=None):
    cm = confusion_matrix(y_true, y_pred)

    fig, ax = plt.subplots(figsize=(6, 5))
    im = ax.imshow(cm, interpolation='nearest', cmap='Blues')
    plt.colorbar(im)

    classes = ['Alive (0)', 'Dead (1)']
    ax.set_xticks([0, 1])
    ax.set_yticks([0, 1])
    ax.set_xticklabels(classes)
    ax.set_yticklabels(classes)
    ax.set_xlabel('Previsto', fontsize=12)
    ax.set_ylabel('Real', fontsize=12)
    ax.set_title(f'Matriz de Confusão — {nome_modelo}', fontsize=13, fontweight='bold')

    for i in range(2):
        for j in range(2):
            ax.text(j, i, str(cm[i, j]), ha='center', va='center',
                    fontsize=16, fontweight='bold',
                    color='white' if cm[i, j] > cm.max() / 2 else 'black')

    plt.tight_layout()
    if salvar and caminho is not None:
        plt.savefig(caminho, dpi=150)
    plt.show()

    print(f'\nVerdadeiros Negativos: {cm[0,0]}')
    print(f'Falsos Positivos: {cm[0,1]}')
    print(f'Falsos Negativos: {cm[1,0]}')
    print(f'Verdadeiros Positivos: {cm[1,1]}')