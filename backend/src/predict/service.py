import joblib
import numpy as np
import pandas as pd
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent.parent

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

CLINICAL_IMPACT = {
    "Reginol_Node_Positive": {
        "threshold": lambda v: v > 5,
        "hipotese": "Alto comprometimento de linfonodos está associado a fadiga persistente, dor linfática localizada e maior risco de linfedema.",
        "sintomas": ["fadiga persistente", "dor linfática", "linfedema", "distress emocional"],
        "fonte": "Fu et al., Integrative Cancer Therapies, 2022 — DOI: 10.1177/15347354221089605"
    },
    "Tumor_Size": {
        "threshold": lambda v: v > 30,
        "hipotese": "Tumores maiores que 2cm estão associados a menor sobrevida livre de doença e maior risco de metástase.",
        "sintomas": ["dor localizada", "pressão na região mamária", "maior risco de metástase"],
        "fonte": "PubMed PMC4156224 — Chinese Academy of Medical Sciences, 1995–1999"
    },
    "Estrogen_Status": {
        "threshold": lambda v: v == "Negative",
        "hipotese": "Tumores estrogênio-negativos tendem a ser mais agressivos e estão associados a menor resposta a terapias hormonais.",
        "sintomas": ["maior agressividade tumoral", "menor resposta a hormonioterapia", "risco aumentado de recorrência"],
        "fonte": "PubMed PMC7982180 — Korean observational study, 2005–2015"
    },
    "Progesterone_Status": {
        "threshold": lambda v: v == "Negative",
        "hipotese": "Status de progesterona negativo combinado com estrogênio negativo está associado a maior risco de metástase cerebral.",
        "sintomas": ["cefaleia", "náuseas", "risco de metástase cerebral", "alterações neurológicas"],
        "fonte": "Cleveland Clinic — Metastatic Breast Cancer; ASCO JCO Oncology Practice, 2023"
    },
    "T_Stage": {
        "threshold": lambda v: v in ["T3", "T4"],
        "hipotese": "Estágios T3 e T4 indicam tumores extensos com possível invasão da parede torácica.",
        "sintomas": ["dor torácica", "dispneia", "comprometimento da parede torácica"],
        "fonte": "Oncology Nurse Advisor — Breast Cancer Symptoms by Stage, 2025"
    },
    "N_Stage": {
        "threshold": lambda v: v in ["N2", "N3"],
        "hipotese": "N2 e N3 indicam comprometimento extenso de linfonodos axilares com possível extensão acima da clavícula.",
        "sintomas": ["dor no pescoço", "dor nos ombros", "inchaço axilar", "risco de disseminação"],
        "fonte": "Breastlink — Enlarged Axillary Lymph Nodes"
    },
    "A_Stage": {
        "threshold": lambda v: v == "Distant",
        "hipotese": "Estágio distante indica metástase em órgãos como ossos, fígado, pulmões ou cérebro.",
        "sintomas": ["dor óssea", "icterícia", "dor abdominal", "tosse crônica", "cefaleia", "náuseas"],
        "fonte": "breastcancer.org — Metastatic Breast Cancer; Cleveland Clinic, 2026"
    },
    "Grade": {
        "threshold": lambda v: v >= 3,
        "hipotese": "Grau 3 e 4 indicam células com alto grau de anormalidade e crescimento rápido da doença.",
        "sintomas": ["fadiga intensa", "perda de apetite", "progressão rápida", "náuseas"],
        "fonte": "Cleveland Clinic — Breast Cancer Overview, 2023"
    },
    "Age": {
        "threshold": lambda v: v < 40,
        "hipotese": "Pacientes mais jovens têm risco 2,3x maior de desenvolver cluster de sintomas: dor, fadiga e distress psicológico.",
        "sintomas": ["fadiga", "dor", "distress psicológico", "impacto nas atividades diárias"],
        "fonte": "PubMed PMC7031174 — Symptom cluster in breast cancer survivors, 2020"
    }
}

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

def gerar_variaveis_impacto(dados: dict, probabilidade: float) -> list:
    if probabilidade < 0.30:
        return []

    impactos = []

    mapeamento = {
        "Reginol_Node_Positive": dados.get("reginol_node_positive"),
        "Tumor_Size":            dados.get("tumor_size"),
        "Estrogen_Status":       dados.get("estrogen_status"),
        "Progesterone_Status":   dados.get("progesterone_status"),
        "T_Stage":               dados.get("t_stage"),
        "N_Stage":               dados.get("n_stage"),
        "A_Stage":               dados.get("a_stage"),
        "Grade":                 dados.get("grade"),
        "Age":                   dados.get("age"),
    }

    for variavel, valor in mapeamento.items():
        if variavel in CLINICAL_IMPACT and valor is not None:
            config = CLINICAL_IMPACT[variavel]
            if config["threshold"](valor):
                impactos.append({
                    "variavel": variavel,
                    "valor":    str(valor),
                    "hipotese": config["hipotese"],
                    "sintomas": config["sintomas"],
                    "fonte":    config["fonte"]
                })

    return impactos

def realizar_predicao(dados: dict) -> dict:
    df = preprocessar(dados)

    probabilidade = float(modelo.predict_proba(df)[0][1])
    predicao      = "Dead" if probabilidade >= 0.5 else "Alive"
    nivel_risco   = definir_risco(probabilidade)
    impactos      = gerar_variaveis_impacto(dados, probabilidade)

    return {
        "predicao": predicao,
        "probabilidade": round(probabilidade * 100, 2),
        "nivel_risco": nivel_risco,
        "variaveis_impacto": impactos
}