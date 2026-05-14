import joblib
import numpy as np
import pandas as pd
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent.parent.parent

scaler         = joblib.load(BASE_DIR / "data-science/models/scaler.joblib")
label_mappings = joblib.load(BASE_DIR / "data-science/models/label_mappings.joblib")
modelo         = joblib.load(BASE_DIR / "data-science/models/random_forest.joblib")

ORDINAL_MAPPINGS = {
    "T_Stage":      {"T1": 1, "T2": 2, "T3": 3, "T4": 4},
    "N_Stage":      {"N1": 1, "N2": 2, "N3": 3},
    "6th_Stage":    {"IIA": 1, "IIB": 2, "IIIA": 3, "IIIB": 4, "IIIC": 5},
    "differentiate": {
        "Well differentiated": 1,
        "Moderately differentiated": 2,
        "Poorly differentiated": 3,
        "Undifferentiated": 4
    }
}

NUM_COLS = ["Age", "Tumor_Size", "Regional_Node_Examined", "Reginol_Node_Positive"]

def preprocessar(dados: dict) -> pd.DataFrame:
    df = pd.DataFrame([{
        "Age":                    dados["age"],
        "Race":                   label_mappings["Race"][dados["race"]],
        "Marital_Status":         label_mappings["Marital_Status"][dados["marital_status"]],
        "T_Stage":                ORDINAL_MAPPINGS["T_Stage"][dados["t_stage"]],
        "N_Stage":                ORDINAL_MAPPINGS["N_Stage"][dados["n_stage"]],
        "6th_Stage":              ORDINAL_MAPPINGS["6th_Stage"][dados["sixth_stage"]],
        "differentiate":          ORDINAL_MAPPINGS["differentiate"][dados["differentiate"]],
        "Grade":                  dados["grade"],
        "A_Stage":                label_mappings["A_Stage"][dados["a_stage"]],
        "Tumor_Size":             dados["tumor_size"],
        "Estrogen_Status":        label_mappings["Estrogen_Status"][dados["estrogen_status"]],
        "Progesterone_Status":    label_mappings["Progesterone_Status"][dados["progesterone_status"]],
        "Regional_Node_Examined": dados["regional_node_examined"],
        "Reginol_Node_Positive":  dados["reginol_node_positive"],
    }])

    df[NUM_COLS] = scaler.transform(df[NUM_COLS])
    return df

def definir_risco(probabilidade: float) -> str:
    if probabilidade < 0.30:
        return "baixo"
    elif probabilidade < 0.60:
        return "moderado"
    return "alto"

def realizar_predicao(dados: dict) -> dict:
    df = preprocessar(dados)

    probabilidade = float(modelo.predict_proba(df)[0][1])
    predicao      = "Dead" if probabilidade >= 0.5 else "Alive"
    nivel_risco   = definir_risco(probabilidade)

    return {
        "predicao":      predicao,
        "probabilidade": round(probabilidade * 100, 2),
        "nivel_risco":   nivel_risco
    }