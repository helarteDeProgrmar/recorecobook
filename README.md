# Book Recommender System (JADE)

This project is a multi-agent system for book recommendation implemented
in Java using the JADE framework.

## 🧠 Agents Overview

The system consists of three agents:

- **VisualizerAgent**: Interacts with the user via console. Collects preferences
and selected author.
- **RecommenderAgent**: Receives user preferences, communicates with the Ingestor
to fetch books, applies a similarity heuristic, and sends back top recommendations.
- **IngestorAgent**: Mock agent that simulates book retrieval by author (can
later be integrated with Google Books API).

## 📁 Project Structure

```txt
├── lib/
│   ├── jade.jar
│   └── json-\*.jar
├── src/
│   ├── Main.java
│   ├── agents/
│   │   ├── VisualizerAgent.java
│   │   ├── RecommenderAgent.java
│   │   └── IngestorAgent.java
│   ├── models/
│   │   └── Book.java
│   └── utils/
│       └── GoogleBooksAPI.java
├── out/  ← compiled .class files
├── compile.sh  ← build & run script

````

## 🚀 How to Run

1. Make sure you have Java 11+ installed.
2. Place `jade.jar` and `json-*.jar` in the `lib/` directory.
3. Use the provided script:

```bash
./compile.sh
````

This will compile and launch the system.

## 🧪 Features

* Collects user preferences on genres via console.
* Fetches books (mocked or real) for a given author.
* Applies a simple heuristic to recommend top 5 most relevant books.
* Uses JADE for agent communication and behavior control.

## 🔧 Requirements

* Java 11 or later
* JADE library (`jade.jar`)
* JSON library (`json-*.jar`, e.g., `json-20210307.jar`)

## 🔮 Future Improvements

* Improve similarity heuristics (e.g., cosine similarity, machine learning).
* Permitir al usuario elegir las recomendaciones.
* GUI interface for user interaction.
* Permitir varios autores.

---
Happy hacking 📚🤖
