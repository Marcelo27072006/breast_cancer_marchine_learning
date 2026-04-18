# 🧬 Breast Cancer Survival Predictor

![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow)
![Python](https://img.shields.io/badge/Python-3.10+-blue)
![License](https://img.shields.io/badge/Uso-Educacional-green)

Projeto desenvolvido na disciplina de **Machine Learning** — 5º Período de Ciência da Computação — **UNINASSAU Aracaju**, como parte do **1º MVP Data IA Health**, alinhado ao **ODS 3 — Saúde e Bem-Estar** da ONU.

---

## 🎯 Objetivo

Desenvolver uma solução baseada em **Aprendizado de Máquina supervisionado** para classificação de risco em pacientes com câncer de mama, prevendo o desfecho clínico — **óbito ou sobrevivência** — com base em variáveis clínicas, tumorais e hormonais. A solução será entregue como uma **aplicação mobile** integrada a um modelo preditivo treinado em Python.

---

## 🧪 Problema

O projeto é modelado como **classificação binária supervisionada**, tendo como variável alvo `Status` (Alive / Dead).

A análise exploratória confirmou padrões estatisticamente significativos que sustentam a viabilidade do modelo:

- Linfonodos positivos → maior mortalidade (p = 1.53e-61)
- Tumores maiores → associados ao óbito (p = 1.24e-17)
- Estrogen Status positivo → maior sobrevivência (p = 2.82e-16)
- Estágio tumoral avançado → menor sobrevivência (p = 8.36e-07)

---

## 📂 Estrutura do Projeto

```text
marchine-breastcancer/
│
├── assets/
│   └── charts_images/
│       ├── correlations/
│       ├── distributions/
│       └── variables_categoricals/
│           └── heatmap/
│
├── data/
│   └── Breast_Cancer.csv
│
├── docs/
│   ├── 00_navegation.md
│   ├── 01_introduction.md
│   ├── 02_data_description.md
│   ├── 03_methodology.md
│   ├── 04_exploratory_analysis.md
│   ├── 05_hypotheses.md
│   ├── 06_conclusions.md
│   └── 07_mvp_description.md
│
├── nootebooks/
│   └── EDA_breast_cancer.ipynb
│
├── requeriments.txt
├── .gitignore
└── README.md
```

---

## 📊 Dataset

Dataset público de pacientes com câncer de mama, disponível no Kaggle.

🔗 Breast Cancer Dataset — Kaggle: https://www.kaggle.com/datasets/reihanenamdari/breast-cancer 

**Autor:** Reihan Enamdari | **Registros:** 4.024 | **Variáveis:** 16 | **Valores nulos:** 0

---

## 🤖 Algoritmos Utilizados

| Algoritmo | Papel | Justificativa |
|---|---|---|
| DummyClassifier | Baseline estatístico | Estabelece o piso mínimo de performance sem aprender padrões |
| Naive Bayes | Modelo 1 | Modelo probabilístico simples e eficiente para classificação binária |
| Random Forest | Modelo 2 | Ensemble robusto a outliers e não-linearidades |
| XGBoost | Modelo 3 | Alta performance em dados tabulares desbalanceados |

---

## 📏 Métricas de Avaliação

| Métrica | Justificativa |
|---|---|
| **AUC-ROC** | Avalia separação entre classes independente do threshold |
| **F1-Score** | Equilíbrio entre Precisão e Recall para a classe minoritária |
| **Recall** | Minimizar falsos negativos — não identificar alto risco é clinicamente crítico |
| **Precisão** | Controle de falsos alarmes desnecessários |

---

## 🛠️ Tecnologias Utilizadas

### EDA e Análise Estatística
- Python, Pandas, NumPy
- Matplotlib, Seaborn
- SciPy, Lifelines

### Machine Learning
- Scikit-learn (DummyClassifier, Naive Bayes, Random Forest)
- XGBoost

### Aplicação
- FastAPI — backend da API
- Flutter — frontend mobile
- Jupyter Notebook, PyCharm
- Git + GitHub

---

## 📱 Aplicação Mobile

A aplicação será desenvolvida em **React Native / Flutter**, integrada a uma API em **FastAPI**, com as seguintes telas previstas:

- Formulário de entrada de dados clínicos do paciente
- Tela de resultado com indicador de risco (baixo / moderado / alto)
- Histórico de avaliações

---

## 💻 Como rodar localmente

### 1. Clonar o repositório

```bash
git clone https://github.com/seu-usuario/marchine-breastcancer.git
cd marchine-breastcancer
```

### 2. Criar ambiente virtual

```bash
python -m venv venv
Windows: venv\Scripts\activate        
Linux/Mac: source venv/bin/activate     
```

### 3. Instalar as dependências

```bash
pip install -r requeriments.txt
```

### 4. Abrir o notebook

```bash
jupyter notebook
```

Depois abra: `nootebooks/EDA_breast_cancer.ipynb`

---

## 📈 Principais Etapas da EDA

1. Inspeção inicial do dataset (`shape`, `info`, `describe`)
2. Verificação de nulos e duplicatas
3. Análise estatística descritiva
4. Identificação de outliers via IQR
5. Matriz de correlação entre variáveis numéricas
6. Análise de variáveis categóricas e heatmaps de proporção
7. Visualizações por grupos (boxplots, pairplot, Kaplan-Meier)
8. Testes de hipóteses (t-test, ANOVA, log-rank)

---

## 🚀 Principais Insights

- Nenhuma variável isolada separa completamente os grupos — o prognóstico é **multifatorial**
- **Reginol Node Positive** foi a variável com maior diferença entre grupos — pacientes mortos tinham em média o dobro de linfonodos positivos
- **Estrogen Status positivo** está associado a 11.79 meses a mais de sobrevivência em média
- **Estado civil** não apresentou influência estatisticamente significativa

---

## 📋 Metodologia Ágil

O projeto utiliza **Kanban** via **Trello**:

`Backlog` → `Em progresso` → `Revisão` → `Concluído`

Trello: https://trello.com/b/GI5CWSvG/breast-cancer-marchine

---

## 👨‍💻 Autores

Desenvolvido por **Marcelo Júnior**, **Leandro Oliveira** e **Marlon Oliveira**  
UNINASSAU Aracaju — Ciência da Computação, 5º Período — 2026.1