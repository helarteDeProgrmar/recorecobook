# RecoRecoBook (RRB)

RecoRecoBook is a multi-agent system for book recommendation implemented
in Java using the JADE framework.

## 🚀 How to Run

The current stable version is: `v0.5`

### Dependencies

Install the json library and the jade library:
[jade.jar](https://jade.tilab.com/download/jade/license/jade-download/)
[json-*.jar](https://github.com/stleary/JSON-java)
Put it in the `lib/`

### Run

1. Make sure you have Java 11+ installed.
2. Place `jade.jar` and `json-*.jar` in the `lib/` directory.
3. Verify you have the `Cabecera.png` beetwen the binary files.

If you use an IDE, run from the Main class.
If you use linux, use the provided script:

```bash
./compile.sh
````

This will compile and launch the system.

## 🤝 How to Contribute

Thank you for your interest in contributing to RecoRecoBook! To ensure effective collaboration, please follow these standard steps:

### 1. Fork the Repository

Go to the repository page and click Fork to create a copy under your GitHub account.

### 2. Clone Your Fork

```bash
git clone https://github.com/your-username/RecoRecoBook.git
cd RecoRecoBook
```

### 3. Create a Branch from develop

Before starting your work, make sure you are working from the develop branch:

```bash
git checkout develop
git pull origin develop
git checkout -b your-feature-branch
```

### 4. Make Your Changes

Work on your feature branch. Make sure to:

- Keep your code clean and consistent.
- Test your changes thoroughly.
- Add comments where necessary.

### 5. Open a Pull Request

Go to your repository on GitHub and click Compare & pull request.
Make sure that:

- The PR targets the develop branch.
- You include a clear and concise description of your changes.

### 7. Wait for Review

Your pull request will be reviewed. You may be asked to make some changes
before it is merged.

## ⚖️ License

This project is licensed under the MIT License. See the LICENSE file for details.

## 🧠 Agents Overview

The system consists of three agents:

- **VisualizerAgent**: Interacts with the user. Collects preferences
selected authors and number of books.
- **RecommenderAgent**: Receives user preferences. communicates with the Ingestor.
Calculate the best books by user preferences.
- **IngestorAgent**: Manage local or external source of books. Agent that take books.
- **TransformerAgent**: Agent that tranform the data from the Google API Books to our
model.

## 📁 Project Structure

```txt
├── README.md
├── compile.sh
├── data
│   ├── genre_keywords.json
│   └── libros.csv
├── lib
│   ├── jade.jar
│   └── json-20210307.jar
├── out
│   └── (compiled .class)
└── src
    ├── Main.java
    ├── Media
    │   └── Cabecera.png
    ├── agents
    │   ├── IngestorAgent.java
    │   ├── RecommenderAgent.java
    │   ├── TransformerAgent.java
    │   └── VisualizerAgent.java
    ├── models
    │   └── Book.java
    ├── ui
    │   ├── AuthorsPanel.java
    │   ├── LoadingPanel.java
    │   ├── PreferencesPanel.java
    │   ├── ResultsPanel.java
    │   └── VisualizerUI.java
    └── utils
        ├── GoogleBooksAPI.java
        ├── HeaderImage.java
        ├── KeywordLoader.java
        └── LocalBooks.java
````


## 🧪 Features

* Collects user preferences on genres via ui.
* Fetches books localy or in the Google API by authors.
* Applies a simple heuristic to recommend top `n` most relevant books.
* Uses JADE for agent communication and behavior control.

## 🔧 Requirements

* Java 11 or later
* JADE library (`jade.jar`)
* JSON library (`json-*.jar`, e.g., `json-20210307.jar`)

## 🔮 Future Improvements

* Improve similarity heuristics (e.g., cosine similarity, machine learning).
* Show better the data of the book.(Included images...)
* Save a session of a user and its preferencies.
* Dinamic filters when the user is watch the results.

---
Happy hacking 📚🤖
