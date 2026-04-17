# 📌 Introdução

Este projeto foi desenvolvido no âmbito da disciplina de **Machine Learning** do curso de Ciência da Computação da UNINASSAU Aracaju, como parte do **1º MVP Data IA Health**, alinhado ao **ODS 3 — Saúde e Bem-Estar** da ONU.

O objetivo é desenvolver uma solução baseada em **Aprendizado de Máquina supervisionado** para análise de dados clínicos de pacientes com câncer de mama, com foco na previsão do agravamento da doença e na classificação do desfecho clínico — **óbito ou sobrevivência**. A proposta consiste em criar um modelo capaz de identificar padrões nos dados e prever a evolução do estado do paciente, classificando perfis de risco com base em variáveis clínicas, tumorais e hormonais. A solução será entregue como uma **aplicação mobile** integrada a um modelo preditivo treinado em Python.

---

## 🎯 Problema

O câncer de mama é a neoplasia mais incidente entre mulheres no Brasil e no mundo. O diagnóstico tardio e a ausência de ferramentas de triagem acessíveis impactam diretamente a sobrevida das pacientes. Identificar precocemente perfis de maior risco pode apoiar profissionais de saúde na tomada de decisão clínica, especialmente em contextos com recursos limitados.

**Público-alvo:** oncologistas, mastologistas e equipes de saúde oncológica.

**Escopo:** classificação binária do desfecho do paciente (Alive / Dead) com base em dados clínicos, utilizando algoritmos clássicos de ML, sem uso de Deep Learning ou AutoML.

---

## 📂 Dataset

O projeto utiliza um dataset público disponível no Kaggle, composto por **4.024 registros** de pacientes diagnosticados com câncer de mama, com **16 variáveis clínicas e demográficas** e sem valores ausentes.

🔗 Breast Cancer Dataset — Kaggle: https://www.kaggle.com/datasets/reihanenamdari/breast-cancer  
**Autor:** Reihan Enamdari | **Uso:** educacional e análise preditiva

As principais variáveis investigadas foram:

- Idade, tamanho do tumor e linfonodos positivos
- Estágio tumoral (T Stage, N Stage, 6th Stage)
- Status hormonal (Estrogênio e Progesterona)
- Grau histológico (Grade e differentiate)
- Tempo de sobrevivência e desfecho clínico (Status)

---

## 🔬 Abordagem

A análise foi estruturada em duas etapas:

**Etapa 1 — Análise Exploratória de Dados (EDA):** compreensão do comportamento das variáveis, identificação de outliers, análise de correlações, visualizações por grupos e testes de hipóteses estatísticas. Os achados confirmaram padrões significativos entre as variáveis clínicas e o desfecho, validando a viabilidade do modelo preditivo.

**Etapa 2 — Machine Learning:** treinamento e comparação de três algoritmos de classificação (Regressão Logística como baseline, Random Forest e XGBoost), avaliados com métricas adequadas para dados desbalanceados, com integração do modelo final a uma aplicação mobile.

---

## 💻 Viabilidade Técnica

A implementação mostrou-se viável pelos seguintes fatores:

- Dataset estruturado, público e com volume adequado para treinamento supervisionado
- Todas as ferramentas são **open-source e gratuitas**, com ampla documentação
- Execução possível em máquinas com recursos computacionais básicos, sem necessidade de GPU
- Arquitetura modular (modelo → API → app mobile) que permite desenvolvimento incremental