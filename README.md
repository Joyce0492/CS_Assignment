# CS Assignments Portfolio

A collection of university assignments demonstrating object-oriented design principles in Java and Python.

---

## Projects

### 1. Bus Tracker App (Java)
**`A2-BusTrackerApp/`**

A Java Swing desktop application for managing bus routes, drivers, and stops.

**Design patterns used:**
- **Composite** — `RouteComponent`, `BusStopLeaf`, and `StopGroupComposite` model individual stops and grouped hubs uniformly, making it easy to calculate total dwell time across any part of a route tree
- **Adapter** — `BusStop` wraps `BusStopLeaf` so the composite structure plugs directly into Swing's `JList` without modification
- **MVC** — `MainFrame` (View), `StopListModel` / `EtaTableModel` (Model), `TransportManager` (Controller)

**Key classes:**

| Class | Role |
|---|---|
| `TransportManager` | Assigns drivers to buses; generates earnings reports |
| `Bus` | Holds a bus number, driver, and list of stops |
| `Driver` | Manages bus allocations with a cap of `MAX_BUSES` |
| `BusStopLeaf` | Leaf node — a single stop with a dwell time |
| `StopGroupComposite` | Composite node — a hub grouping multiple stops |
| `BusStop` | Adapter exposing `BusStopLeaf` to the Swing UI |
| `MainFrame` | Swing GUI entry point |

---

### 2. Treasure Hunt Game (Python)
**`treasure_hunt.py`**

A two-player terminal game where hunters take turns collecting randomly placed treasures. The first hunter to accumulate over 1000 points wins.

**Concepts demonstrated:**
- Object-oriented design with clear class responsibilities
- Custom `Stack` data structure (LIFO) for treasure management
- Input validation with robust error handling
- Euclidean distance calculation to determine treasure value

**Game rules:**
- Treasures are placed randomly on a grid from (−10, −10) to (10, 10)
- Value is determined by distance from the origin:

  | Distance | Value |
  |---|---|
  | ≤ 2 units | 500 pts |
  | 2–5 units | 200 pts |
  | 5–10 units | 50 pts |
  | > 10 units | 0 pts |

- Hunters alternate turns drawing from a stack of treasures
- Game ends when a hunter exceeds 1000 points or all treasures are exhausted
- Scores persist across multiple rounds if players choose to continue

**Key classes:**

| Class | Role |
|---|---|
| `Stack` | LIFO container for treasure management |
| `Treasure` | Stores coordinates; computes distance and value |
| `Hunter` | Tracks a player's collected score |
| `TreasureMap` | Game controller — generates treasures and runs rounds |

---

## Technologies

- **Java** — Swing GUI, OOP, design patterns
- **Python 3** — OOP, data structures, terminal I/O
