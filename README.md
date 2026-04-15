# 📊 Breast Cancer Survival Analysis — EDA & Statistical Hypotheses

Projeto de **Análise Exploratória de Dados (EDA)** aplicado a um dataset clínico de **câncer de mama**, com foco na investigação de padrões estatísticos, outliers, correlações e hipóteses relacionadas à sobrevivência dos pacientes.

---

## 🎯 Objetivo

O objetivo deste projeto é compreender como variáveis clínicas, tumorais e hormonais impactam o **tempo de sobrevivência dos pacientes**, utilizando análise exploratória e testes estatísticos.

A análise busca responder perguntas como:

* Pacientes com status hormonal positivo sobrevivem mais?
* Tumores maiores estão associados ao óbito?
* O número de linfonodos positivos influencia a sobrevida?
* O estágio do tumor afeta o prognóstico?

---

## 📂 Estrutura do Projeto

```text
marchine-breastcancer/
│
├── charts_Images/
│   ├── 6th_Stage.png
│   ├── A_Stage.png
│   ├── averageMaritalStatusSurvival.png
│   ├── correlationMatriz.png
│   ├── correlationTumorSizeStatus.png
│   ├── differentiate.png
│   ├── Estrogen_Status.png
│   ├── Grade.png
│   ├── Marital_Status.png
│   ├── N_Stage.png
│   ├── positiveLinfonodos.png
│   ├── Progesterone_Status.png
│   ├── Race.png
│   ├── Status.png
│   ├── survivorEstrogenStatus.png
│   ├── T_Stage_.png
│   └── tumorSizeSurvival.png
│
├── data/
│   └── Breast_Cancer.csv
│
├── docs/
│   ├── introduction.md
│   ├── methodology.md
│   ├── exploratory_analysis.md
│   ├── hypotheses.md
│   └── conclusions.md
│
├── notebook/
│   └── EDA_breast_cancer.ipynb
│
├── .gitignore
└── README.md
```

---

## 💻 Como rodar no seu computador

Siga os passos abaixo para executar o projeto localmente.

### 1. Clonar o repositório

```bash
git clone https://github.com/seu-usuario/marchine-breastcancer.git
```

### 2. Entrar na pasta do projeto

```bash
cd marchine-breastcancer
```

### 3. Criar ambiente virtual

No Windows:

```bash
python -m venv venv
venv\Scripts\activate
```

### 4. Instalar as dependências

```bash
pip install pandas numpy matplotlib seaborn scipy notebook
```

### 5. Abrir o notebook

```bash
jupyter notebook
```

Depois abra:

```text
notebook/EDA_breast_cancer.ipynb
```

---

## 📈 Principais Etapas da EDA

### 🔹 Análise Descritiva

Foram calculadas as principais medidas estatísticas:

* média
* mediana
* desvio padrão
* variância
* amplitude
* IQR

---

### 🔹 Identificação de Outliers

Os outliers foram identificados utilizando o método do **IQR (Intervalo Interquartil)**.

### Resultados encontrados

* **Tumor Size:** 222 outliers altos
* **Regional Node Examined:** 72 outliers altos
* **Reginol Node Positive:** 344 outliers altos
* **Survival Months:** 18 outliers baixos

---

### 🔹 Correlação

Foi construída uma matriz de correlação entre as variáveis numéricas.

As principais relações observadas foram:

* maior associação entre linfonodos examinados e positivos
* relação negativa entre linfonodos positivos e sobrevivência

---

## 🧪 Hipóteses Estatísticas

Foram realizados testes estatísticos para validar hipóteses extraídas da EDA.

### 1. Pacientes positivos sobrevivem mais

* **p-valor:** 2.81e-16
* **Média Positive:** 72.09
* **Média Negative:** 60.30

✔ Existe diferença significativa

---

### 2. Pacientes que morreram tinham tumores maiores

* **p-valor:** 1.23e-17
* **Média Alive:** 29.27
* **Média Dead:** 37.14

✔ Existe diferença significativa

---

### 3. Pacientes que morreram tinham mais linfonodos positivos

* **p-valor:** 1.52e-61
* **Média Alive:** 3.60
* **Média Dead:** 7.24

✔ Existe diferença significativa

---

### 4. Pacientes positivos possuem tumores menores

* **p-valor:** 0.00015

✔ Existe diferença significativa

---

### 5. O estágio do tumor influencia a sobrevivência

* **p-valor:** 8.36e-07

✔ Existe diferença significativa

---

### 6. Pacientes casados sobrevivem mais

* **p-valor:** 0.124

✘ Não existe diferença significativa

---

## 📊 Visualizações

### 🔹 Matriz de Correlação

![Matriz de Correlação](../charts_Images/correlationMatriz.png)

### 🔹 Sobrevivência x Tamanho do Tumor

![Tumor Size x Survival](../charts_Images/tumorSizeSurvival.png)

### 🔹 Sobrevivência por Estrogen Status

![Survival Estrogen Status](../charts_Images/survivorEstrogenStatus.png)

### 🔹 Média de Sobrevivência por Estado Civil

![Average Marital Status Survival](../charts_Images/averageMaritalStatusSurvival.png)

### 🔹 Linfonodos Positivos

![Positive Linfonodos](../charts_Images/positiveLinfonodos.png)

---

## 📘 Documentação

A documentação detalhada está disponível na pasta `docs/`.

Arquivos incluídos:

* introdução
* metodologia
* análise exploratória
* hipóteses
* conclusão

---

## 🛠 Tecnologias Utilizadas

* Python
* Pandas
* NumPy
* Matplotlib
* Seaborn
* SciPy
* Jupyter Notebook
* PyCharm

---

## 🚀 Principais Insights

A análise indicou que fatores como:

* tamanho do tumor
* linfonodos positivos
* estágio tumoral
* status hormonal

possuem forte relação com a sobrevivência dos pacientes.

---

## 👨‍💻 Autor

Desenvolvido por **Marcelo Júnior**, **Leandro Oliveira** e **Marlon Oliveira**.
