# 🧬 Breast Cancer Survival Predictor

![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow)
![Python](https://img.shields.io/badge/Python-3.10+-blue)
![License](https://img.shields.io/badge/Uso-Educacional-green)
![Docker](https://img.shields.io/badge/Docker-Containerizado-blue)

Projeto desenvolvido na disciplina de **Machine Learning** — 5º Período de Ciência da Computação — **UNINASSAU Aracaju**, como parte do **1º MVP Data IA Health**, alinhado ao **ODS 3 — Saúde e Bem-Estar** da ONU.

---

## 🎯 Objetivo

Desenvolver uma solução baseada em **Aprendizado de Máquina supervisionado** para classificação de risco em pacientes com câncer de mama, prevendo o desfecho clínico — **óbito ou sobrevivência** — com base em variáveis clínicas, tumorais e hormonais. A solução é entregue como uma **aplicação mobile** integrada a uma API REST com modelo preditivo treinado em Python.

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

```
marchine-breastcancer/
│
├── 🖥️  backend/                    # API REST — FastAPI + PostgreSQL
│   ├── src/
│   │   ├── core/                   # Configuração e segurança (API Key)
│   │   ├── database/               # Conexão com PostgreSQL via SQLAlchemy
│   │   └── predict/                # Modelos, schemas, rotas e serviço de predição
│   ├── Dockerfile
│   └── requirements.txt
│
├── 📊  data-science/               # Núcleo de ciência de dados e ML
│   ├── assets/                     # Gráficos e visualizações geradas
│   ├── data/                       # Dataset bruto e processado (CSV)
│   ├── models/                     # Modelos e artefatos serializados (joblib)
│   └── notebooks/                  # Análises e modelos (Jupyter)
│
├── 📚  docs/                       # Documentação técnica do projeto
│
├── 📱  frontend/                   # Aplicação mobile — Kotlin (Android)
│
├── docker-compose.yml
├── .env.example
├── .gitignore
└── README.md
```

### 🗂️ Descrição dos Módulos

| Diretório | Tecnologia | Responsabilidade |
|---|---|---|
| `backend/` | FastAPI · PostgreSQL · SQLAlchemy | API REST com predição, persistência e hipóteses clínicas |
| `data-science/assets/` | Matplotlib · Seaborn | Visualizações e gráficos gerados durante a EDA |
| `data-science/data/` | CSV | Dataset bruto do Kaggle e dados processados |
| `data-science/models/` | Joblib | Modelos treinados, scaler e label mappings |
| `data-science/notebooks/` | Jupyter Notebook | EDA, pré-processamento e treinamento dos modelos |
| `docs/` | Markdown | Documentação técnica completa do projeto |
| `frontend/` | Kotlin · Android | Aplicação mobile com formulário e resultado de risco |

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
- Imbalanced-learn (SMOTE)

### Backend
- FastAPI — API REST
- SQLAlchemy — ORM
- PostgreSQL — banco de dados
- Joblib — serialização dos modelos
- Autenticação por API Key

### Aplicação
- Kotlin — frontend mobile (Android)
- Docker + Docker Compose — containerização
- Jupyter Notebook, PyCharm, Android Studio, VS Code
- Git + GitHub

---

## 🔌 API — Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/` | Health check |
| `POST` | `/predicao/` | Realiza predição e salva no banco |
| `GET` | `/predicao/` | Lista todas as predições |
| `GET` | `/predicao/{id}` | Busca predição por ID |

Todos os endpoints exigem autenticação via header `X-API-Key`.

### Exemplo de resposta

```json
{
  "id": "id_criptografado",
  "paciente_nome": "João Silva",
  "predicao": "Alive",
  "probabilidade": 25.0,
  "nivel_risco": "baixo",
  "criado_em": "2026-05-15T01:46:50.664776",
  "aviso": "As hipóteses apresentadas são baseadas em literatura científica e têm caráter informativo. Não substituem avaliação médica especializada.",
  "variaveis_impacto": []
}
```

---

## 📱 Aplicação Mobile

A aplicação é desenvolvida em **Kotlin**, integrada à API em **FastAPI**, com as seguintes telas:

- Formulário de entrada de dados clínicos do paciente
- Tela de resultado com indicador de risco (baixo / moderado / alto)
- Hipóteses clínicas baseadas em literatura científica
- Histórico de avaliações

---

## 💻 Como rodar localmente

### 1. Clonar o repositório

```bash
git clone https://github.com/Marcelo27072006/breast_cancer_marchine_learning
cd marchine-breastcancer
```

### 2. Configurar o ambiente

```bash
cp .env.example .env
# Preencher DATABASE_URL e API_KEY no .env e os dados para acesso do banco
```

### 3. Rodar com Docker

```bash
docker-compose up --build -d
```

A API estará disponível em `http://localhost:8000/docs`.

### 4. Rodar sem Docker

```bash
# Data Science
cd data-science
pip install -r requirements.txt
jupyter notebook

# Backend (em outro terminal)
cd backend
pip install -r requirements.txt
python -m uvicorn main:app --reload
```

---

## 🧩 Artefatos do Modelo

Localizados em `data-science/models/`:

| Arquivo | Descrição |
|---|---|
| `scaler.joblib` | MinMaxScaler fitado no treino |
| `label_mappings.joblib` | Mapeamentos do Label Encoding |
| `random_forest.joblib` | Modelo Random Forest treinado |
| `naive_bayes.joblib` | Modelo Naive Bayes treinado |
| `xgboost.joblib` | Modelo XGBoost treinado |

---

## 📈 Principais Etapas da EDA

1. Inspeção inicial do dataset (`shape`, `info`, `describe`)
2. Verificação de nulos e duplicatas
3. Análise estatística descritiva
4. Identificação de outliers via IQR
5. Matriz de correlação entre variáveis numéricas
6. Análise de variáveis categóricas e heatmaps de proporção
7. Visualizações por grupos (boxplots, pairplot)
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

Desenvolvido por **Marcelo Júnior** e **Leandro Oliveira**
UNINASSAU Aracaju — Ciência da Computação, 5º Período — 2026.1
