# 📊 Análise Exploratória de Dados

A análise exploratória foi realizada com o objetivo de compreender o comportamento das variáveis, identificar padrões, detectar outliers e analisar possíveis relações entre fatores clínicos e o tempo de sobrevivência dos pacientes.

---

## 🔢 Análise das Variáveis Numéricas

Foram analisadas as seguintes variáveis:

- Age  
- Tumor Size  
- Regional Node Examined  
- Reginol Node Positive  
- Survival Months  

Foram calculadas medidas como média, mediana, desvio padrão e intervalo interquartil (IQR), permitindo avaliar a distribuição dos dados.

---
### 🔹 Dados Duplicados

Foi identificada a presença de **1 registro duplicado** no dataset.

Após verificação, constatou-se que se trata de uma duplicata exata, não agregando informação adicional à análise.

Dessa forma, o registro duplicado foi removido, garantindo maior consistência dos dados.

---

## 📈 Identificação de Outliers

A detecção de outliers foi realizada utilizando o método do **Intervalo Interquartil (IQR)**.

### 🔍 Resultados

- **Age:** não apresentou outliers  
- **Tumor Size:** 222 outliers altos  
- **Regional Node Examined:** 72 outliers altos  
- **Reginol Node Positive:** 344 outliers altos  
- **Survival Months:** 18 outliers baixos  

---

## 🧠 Interpretação dos Outliers

- A variável **Age** apresentou distribuição estável, sem valores extremos, indicando consistência nos dados demográficos.

- Os outliers em **Tumor Size** representam pacientes com tumores significativamente maiores, possivelmente associados a estágios mais avançados e maior agressividade da doença.

- Em **Regional Node Examined**, os valores extremos sugerem pacientes submetidos a análises clínicas mais extensas, possivelmente em casos de maior complexidade.

- A variável **Reginol Node Positive** apresentou a maior quantidade de outliers, indicando pacientes com elevado comprometimento nodal, fator diretamente relacionado ao agravamento da doença.

- Os outliers baixos em **Survival Months** representam pacientes com tempo de sobrevivência muito reduzido, podendo indicar evolução rápida da doença ou menor resposta ao tratamento.

---

## 🔗 Relação entre Variáveis

### 📉 Tamanho do tumor vs Sobrevivência

A análise gráfica indicou que:

> **Quanto maior o tamanho do tumor, menor tende a ser o tempo de sobrevivência.**

Esse comportamento sugere uma relação inversa entre a progressão tumoral e o prognóstico do paciente.

---

### 📉 Linfonodos positivos vs Sobrevivência

A regressão mostrou que:

> O aumento no número de linfonodos positivos está associado à redução do tempo de sobrevivência.

Esse resultado reforça o papel dos linfonodos como indicador importante de gravidade da doença.

---

## 📊 Análise de Correlação

A matriz de correlação revelou:

- Correlação positiva moderada entre  
  **Regional Node Examined** e **Reginol Node Positive** *(r ≈ 0.41)*  

- Correlação positiva fraca entre  
  **Tumor Size** e **Reginol Node Positive** *(r ≈ 0.24)*  

- Correlação negativa fraca entre  
  **Reginol Node Positive** e **Survival Months** *(r ≈ -0.14)*  

- A variável **Age** não apresentou relação significativa com a sobrevivência  

---

### 🔍 Interpretação

- Pacientes com mais linfonodos examinados tendem a apresentar maior número de linfonodos comprometidos.  
- O aumento do comprometimento nodal está associado à redução da sobrevivência.  
- O tamanho do tumor possui influência indireta, possivelmente relacionado à progressão da doença.  

---

## 🧩 Análise das Variáveis Categóricas

Foram analisadas as distribuições de frequência das variáveis:

- Race  
- Marital Status  
- T Stage  
- N Stage  
- Estrogen Status  
- Progesterone Status  
- Status  

### 🔍 Observações

- A distribuição dos estágios tumorais indica predominância de estágios intermediários.  
- O status hormonal apresenta diferenças relevantes entre os grupos.  
- A variável **Status (Alive/Dead)** permite comparações diretas com variáveis clínicas.  


---

## 📊 Análise de Distribuições

Foram analisadas distribuições condicionais entre variáveis, destacando:

- **Sobrevivência por status hormonal (Estrogen Status)**  
- **Tamanho do tumor por status do paciente (Alive/Dead)**  
- **Sobrevivência por estado civil**

Essas análises permitem visualizar diferenças entre grupos e identificar padrões relevantes.

### 🟥 Heatmaps de Proporção

**N Stage × Status**

A taxa de mortalidade aumenta progressivamente com o avanço do estágio nodal. Pacientes em **N1** apresentam a menor proporção de óbitos, enquanto **N3** concentra a maior taxa de mortalidade relativa.

**Grade × T Stage**

Tumores de **Grade 1** concentram maior proporção de estágios iniciais (T1, T2), enquanto **Grade 3** apresenta maior presença de estágios avançados (T3, T4), sugerindo associação entre grau histológico e progressão tumoral.
### 📦 Boxplot das Variáveis Numéricas por Status

Os boxplots compararam a distribuição das variáveis numéricas entre pacientes **Alive** e **Dead**, permitindo visualizar diferenças de mediana, dispersão e outliers entre os grupos.

- **Age:** distribuição similar entre os grupos, sem diferença expressiva na mediana
- **Tumor Size:** pacientes que vieram a óbito apresentaram mediana de tamanho tumoral superior
- **Regional Node Examined:** distribuição semelhante entre os grupos, com maior dispersão nos pacientes falecidos
- **Reginol Node Positive:** diferença mais evidente — pacientes falecidos apresentaram mediana significativamente maior, reforçando o papel do comprometimento nodal no prognóstico
- **Survival Months:** pacientes **Dead** concentram tempos de sobrevivência menores, como esperado

---

### 🔵 Pairplot das Variáveis Numéricas por Status

O pairplot visualiza simultaneamente todas as relações entre variáveis numéricas, com pontos coloridos pelo desfecho do paciente e KDE na diagonal.

- A separação entre **Alive** e **Dead** é mais evidente nas variáveis de linfonodos e tempo de sobrevivência
- A maioria das variáveis apresenta sobreposição considerável, indicando que nenhuma isolada é suficiente para separar os desfechos
- A combinação de **Reginol Node Positive** e **Survival Months** apresenta a separação mais visível entre os grupos
---

## 📌 Conclusão da EDA

A análise exploratória evidenciou que os principais fatores associados ao tempo de sobrevivência são:

- tamanho do tumor  
- número de linfonodos positivos  
- estágio da doença  
- status hormonal  

Essas variáveis demonstram forte potencial explicativo e serviram como base para a formulação das hipóteses estatísticas testadas na etapa seguinte.