# ✅ Conclusão

A análise exploratória permitiu identificar padrões relevantes no comportamento das variáveis clínicas, estabelecendo uma base sólida para a modelagem preditiva.

## Principais insights

- Maior tamanho tumoral está associado a pior prognóstico
- Maior número de linfonodos positivos reduz a sobrevida — resultado estatisticamente mais expressivo da análise (p = 1.53e-61)
- Estágios avançados (T3, T4 e N3) apresentam menor tempo de sobrevivência
- Pacientes com Estrogen Status positivo sobrevivem em média 11.79 meses a mais do que os negativos
- O grau histológico (Grade) mostrou associação com o avanço do estágio tumoral
- O estado civil não demonstrou influência estatisticamente significativa na sobrevivência
- Nenhuma variável isolada separa completamente os grupos Alive/Dead — o prognóstico é multifatorial

## Limitações observadas

- O dataset apresenta **desbalanceamento** entre as classes Alive e Dead, o que deve ser considerado na modelagem
- A variável **Reginol Node Positive** contém erro tipográfico no nome original do dataset, mantido para consistência
- A ausência de censura explícita limita análises de sobrevivência mais rigorosas

## Próximos passos

Como evolução do projeto, recomenda-se:

- Pré-processamento: encoding de variáveis categóricas, normalização e tratamento do desbalanceamento 
- Treinamento e comparação dos modelos de classificação: **DummyClassifier** (baseline estatístico), **Naive Bayes**, **Random Forest** e **XGBoost**
- Avaliação com métricas adequadas para dados desbalanceados: AUC-ROC, F1-Score, Recall e Precisão
- Seleção e justificativa do modelo final com base na comparação de resultados
- Integração do modelo final a uma **aplicação mobile**