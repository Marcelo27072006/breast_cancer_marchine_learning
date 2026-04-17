# 📊 Descrição dos Dados

O dataset utilizado neste projeto é composto por informações clínicas e demográficas de pacientes diagnosticados com câncer de mama, com o objetivo de analisar fatores que influenciam o tempo de sobrevivência.

Os dados são estruturados e contêm **4.024 registros**, sem valores ausentes, permitindo uma análise consistente e confiável.


---

## 📌 Variáveis do Dataset

A seguir, são apresentadas as variáveis disponíveis no conjunto de dados:

---

### 🔹 Variáveis Numéricas

- **Age**  
  Representa a idade do paciente no momento do diagnóstico.

- **Tumor Size**  
  Indica o tamanho do tumor, sendo um dos principais fatores clínicos associados ao prognóstico.

- **Regional Node Examined**  
  Quantidade de linfonodos examinados durante o diagnóstico.

- **Reginol Node Positive**  
  Número de linfonodos positivos (afetados pelo câncer).

- **Survival Months**  
  Tempo de sobrevivência do paciente, em meses.

---

### 🔹 Variáveis Categóricas

- **Race**  
  Classificação étnica do paciente.

- **Marital Status**  
  Estado civil do paciente (casado, solteiro, etc.).

- **T Stage**  
  Estágio do tumor primário, relacionado ao seu tamanho e extensão.

- **N Stage**  
  Estágio que indica o comprometimento dos linfonodos.

- **6th Stage**  
  Classificação geral do estágio do câncer baseada em critérios clínicos.

- **differentiate**  
  Grau de diferenciação celular do tumor (nível de agressividade).

- **Grade**  
  Classificação do tumor com base na aparência das células cancerígenas.

- **A Stage**  
  Indicação da presença ou ausência de metástase.

- **Estrogen Status**  
  Indica se o tumor é sensível ao hormônio estrogênio (positivo ou negativo).

- **Progesterone Status**  
  Indica se o tumor é sensível ao hormônio progesterona.

- **Status**  
  Situação do paciente ao final do acompanhamento (vivo ou óbito).

---

## 📈 Considerações sobre os Dados

- O dataset não apresenta valores nulos, eliminando a necessidade de tratamento de dados ausentes.  
- As variáveis numéricas permitem análises estatísticas e identificação de outliers.  
- As variáveis categóricas possibilitam análises comparativas entre grupos.  
- A variável **Survival Months** é considerada a principal variável de interesse (**target**), sendo utilizada para avaliar o impacto das demais variáveis.

---

## 🎯 Relevância para o Projeto

A combinação de variáveis clínicas e demográficas torna este dataset adequado para:

- análise de padrões de sobrevivência  
- identificação de fatores de risco  
- validação de hipóteses estatísticas  
- suporte a futuras modelagens preditivas  

---

## 📊 Dataset

Este projeto utiliza um dataset público disponível no Kaggle:

🔗 https://www.kaggle.com/datasets/reihanenamdari/breast-cancer

Autor: Reihan Enamdari  
Uso: fins educacionais e análise exploratória de dados