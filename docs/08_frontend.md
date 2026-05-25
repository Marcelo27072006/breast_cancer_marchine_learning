# 📱 Frontend — Breast Cancer Survival Predictor

## Visão Geral

Aplicação mobile desenvolvida em **Kotlin** com **Jetpack Compose**, responsável por coletar os dados clínicos do paciente, enviar para a API e exibir o resultado da predição com indicador de risco e hipóteses clínicas baseadas em literatura científica.

---

## 📂 Estrutura

```
frontend/
└── app/src/main/java/com/example/signa/
    ├── data/
    │   ├── api/
    │   │   ├── PredicaoApiService.kt   # Interface Retrofit com os endpoints
    │   │   └── RetrofitClient.kt       # Configuração do cliente HTTP
    │   ├── model/
    │   │   └── PredicaoModels.kt       # Data classes de Request e Response
    │   └── repository/
    │       └── PredicaoRepository.kt   # Comunicação com a API
    ├── ui/
    │   ├── screens/
    │   │   ├── HomeScreen.kt           # Dashboard principal com resultado
    │   │   └── pacientes.kt            # Formulário de entrada de dados
    │   └── viewmodel/
    │       └── PredicaoViewModel.kt    # Estado da UI e lógica de negócio
    └── MainActivity.kt                 # Ponto de entrada e navegação
```

---

## 🖥️ Telas

### Dashboard — `HomeScreen.kt`
Tela principal exibida ao abrir o app. Mostra:

- **Círculo de status** — indicador visual do nível de risco (Estável / Possível Agravamento / Agravamento) com cores e ícones
- **Card de probabilidade** — percentual de risco com barra de progresso
- **Card do paciente** — nome, predição (Alive/Dead) e nível de risco
- **Variáveis de Impacto** — hipóteses clínicas baseadas em literatura para os fatores de risco identificados, exibidas apenas para risco moderado e alto
- **Aviso legal** — disclaimer informando que as hipóteses não substituem avaliação médica

### Formulário — `pacientes.kt`
Tela de entrada de dados clínicos organizada em seções:

| Seção | Campos |
|---|---|
| Identificação | Nome do paciente |
| Dados Pessoais | Idade, Raça, Estado Civil |
| Dados do Tumor | Tamanho, Estágio T, Grau, Estágio A, Diferenciação Celular, Estadiamento, Estágio N |
| Linfonodos | Examinados, Positivos |
| Status Hormonal | Estrogênio, Progesterona |

Todos os campos categóricos são exibidos em **português** — o valor enviado para a API permanece em inglês para compatibilidade com o modelo.

---

## 🔄 Fluxo da Aplicação

```
HomeScreen
    ↓ usuário clica no ✎
PacientesScreen (formulário)
    ↓ usuário preenche e clica em "Executar Predição"
PredicaoViewModel.enviarPredicao()
    ↓ POST /predicao/ com X-API-Key
API FastAPI
    ↓ retorna predicao, probabilidade, nivel_risco, variaveis_impacto
PredicaoViewModel salva variaveis_impacto no cache (impactosCache)
    ↓ navega automaticamente de volta
HomeScreen exibe o resultado completo
```

---

## 🗂️ Arquitetura

O projeto segue o padrão **MVVM (Model-View-ViewModel)**:

| Camada | Arquivo | Responsabilidade |
|---|---|---|
| **Model** | `PredicaoModels.kt` | Data classes de Request/Response |
| **Repository** | `PredicaoRepository.kt` | Comunicação com a API via Retrofit |
| **ViewModel** | `PredicaoViewModel.kt` | Estado da UI, cache e lógica de negócio |
| **View** | `HomeScreen.kt`, `pacientes.kt` | Composables que observam o ViewModel |

---

## 💾 Cache de Variáveis de Impacto

As `variaveis_impacto` são geradas dinamicamente pelo backend e **não são salvas no banco de dados**. Para preservá-las enquanto o app está aberto, o `PredicaoViewModel` mantém um cache em memória:

```kotlin
private val impactosCache = mutableMapOf<String, List<VariavelImpacto>>()
```

- Ao receber uma predição nova (POST), as variáveis são salvas no cache pelo ID
- Ao carregar o histórico (GET), o `HomeScreen` injeta as variáveis do cache via `predicao.copy(variaveisImpacto = vm.getImpactosParaId(predicao.id))`
- O cache é perdido ao fechar o app — comportamento esperado para o MVP

---

## 🔐 Autenticação

A API Key é configurada no `build.gradle.kts` via `BuildConfig` e injetada automaticamente em todo request pelo `apiKeyInterceptor` no `RetrofitClient`:

```kotlin
private val apiKeyInterceptor = Interceptor { chain ->
    val request = chain.request().newBuilder()
        .addHeader("X-API-Key", BuildConfig.API_KEY)
        .build()
    chain.proceed(request)
}
```

---

## 🌐 Configuração de Rede

A URL base é configurada via `BuildConfig.BASE_URL` no `build.gradle.kts`:

- **Emulador:** `http://10.0.2.2:8000` — alias do Android para localhost do PC
- **Dispositivo físico:** IP da máquina na rede local (ex: `http://192.168.1.x:8000`)
- **Produção:** URL do servidor Railway

O `network_security_config.xml` permite tráfego HTTP em desenvolvimento:

```xml
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
        <domain includeSubdomains="true">192.168.100.112</domain>
        <domain includeSubdomains="true">localhost</domain>
    </domain-config>
</network-security-config>
```

> Em produção com HTTPS essa configuração pode ser removida.

---

## 🛠️ Tecnologias

| Tecnologia | Função |
|---|---|
| Kotlin | Linguagem principal |
| Jetpack Compose | UI declarativa |
| Retrofit | Cliente HTTP para comunicação com a API |
| OkHttp | Interceptors (API Key, logging) |
| Gson | Deserialização do JSON |
| ViewModel + StateFlow | Gerenciamento de estado reativo |
| Navigation Compose | Navegação entre telas |

---

## 📦 Dependências principais

```gradle
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
implementation("androidx.navigation:navigation-compose:2.7.7")
```