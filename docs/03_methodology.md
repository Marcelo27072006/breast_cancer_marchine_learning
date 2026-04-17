# 🧪 Metodologia

O projeto foi conduzido em duas etapas principais: **Análise Exploratória de Dados (EDA)** e **Modelagem Preditiva com Machine Learning**, ambas desenvolvidas em **Python**, com bibliotecas amplamente utilizadas na área de ciência de dados.

---

## 🛠️ Ferramentas Utilizadas

### EDA e Análise Estatística

- **Pandas** — manipulação e análise de dados estruturados
- **NumPy** — operações matemáticas e manipulação de arrays
- **Matplotlib** — criação de gráficos e visualizações
- **Seaborn** — visualizações estatísticas avançadas
- **SciPy** — testes estatísticos (Teste t de Student, ANOVA, Log-rank)
- **Lifelines** — análise de sobrevivência (curvas de Kaplan-Meier)

### Machine Learning

- **Scikit-learn** — treinamento e avaliação dos modelos (DummyClassifier, Naive Bayes e Random Forest)
- **XGBoost** — algoritmo de gradient boosting para classificação
- **Scikit-learn Metrics** — cálculo das métricas de avaliação (AUC-ROC, F1-Score, Recall, Precisão)

---

## 🔍 Etapas da Análise

### Fase 1 — Análise Exploratória de Dados (EDA)

1. Leitura e inspeção inicial do dataset
2. Verificação de tipos de dados, nulos e duplicatas
3. Análise estatística descritiva
4. Identificação de outliers pelo método IQR
5. Análise de correlação entre variáveis numéricas
6. Análise de distribuição das variáveis categóricas e heatmaps de proporção
7. Visualizações por grupos (boxplots, pairplot, curvas de Kaplan-Meier)
8. Testes de hipóteses estatísticas

### Fase 2 — Modelagem Preditiva

1. Pré-processamento dos dados (encoding, normalização, tratamento do desbalanceamento)
2. Divisão entre conjuntos de treino e teste
3. Treinamento do modelo baseline (DummyClassifier)
4. Treinamento dos modelos adicionais (Naive Bayes, Random Forest e XGBoost)
5. Avaliação e comparação dos modelos com métricas definidas
6. Seleção e justificativa do modelo final

---

## 📊 Método para Identificação de Outliers

Para a detecção de valores discrepantes, foi utilizado o método do **Intervalo Interquartil (IQR)**.

### 🔹 Cálculo do IQR

$$
IQR = Q_3 - Q_1
$$

Onde:

- $Q_1$ representa o primeiro quartil (25%)
- $Q_3$ representa o terceiro quartil (75%)

### 🔹 Definição dos Limites

$$
LI = Q_1 - 1.5 \cdot IQR
$$

$$
LS = Q_3 + 1.5 \cdot IQR
$$

Onde:

- **LI (Limite Inferior)** identifica outliers baixos
- **LS (Limite Superior)** identifica outliers altos

Valores abaixo de $LI$ ou acima de $LS$ são considerados outliers.

---

## 🎯 Tipo de Problema e Algoritmos

O problema foi modelado como **classificação binária supervisionada**, tendo como variável alvo `Status` (Alive / Dead).

Foram selecionados quatro algoritmos, sendo um baseline estatístico e três modelos conceitualmente distintos:

| Algoritmo | Papel | Justificativa |
|---|---|---|
| DummyClassifier | Baseline estatístico | Classifica com base na distribuição das classes sem aprender padrões. Estabelece o piso mínimo de performance — qualquer modelo real deve superá-lo para ser considerado válido |
| Naive Bayes | Modelo 1 | Modelo probabilístico baseado no Teorema de Bayes. Simples, rápido e eficiente para classificação binária, serve como primeiro modelo real de comparação |
| Random Forest | Modelo 2 | Ensemble baseado em árvores, robusto a outliers e não-linearidades |
| XGBoost | Modelo 3 | Gradient boosting de alta performance em dados tabulares desbalanceados |

---

## 📏 Métricas de Avaliação

Dado o desbalanceamento entre as classes, foram escolhidas métricas adequadas para esse contexto:

| Métrica | Justificativa |
|---|---|
| **AUC-ROC** | Avalia a separação entre classes independente do threshold |
| **F1-Score** | Equilíbrio entre Precisão e Recall para a classe minoritária |
| **Recall** | Prioridade em minimizar falsos negativos — não identificar um paciente de alto risco é clinicamente crítico |
| **Precisão** | Controle de falsos alarmes desnecessários |