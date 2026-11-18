# Java Data Structures Projects

Two Java Swing desktop applications for  Data Structures & Algorithms 

## Projects

### 1. Mobile Money Transaction Manager (`mobile-money/`)

Loads mobile money transactions from a text file and lets you search, sort and total them in a table view.

**Features**
- Load transactions from `transactions.txt` (reference number, sender, date, amount, receiver phone, receiver name)
- Linear search by reference number
- Binary search by reference number (the array is insertion-sorted by reference first)
- Bubble sort by amount, with an early exit when the data is already sorted
- Total transacted amount in Kwacha (K)

**Data structure:** a fixed-size array of `Transaction` objects, with a count of active records.

### 2. Java Programming Competition Manager (`competition-menu/`)

Manages teams through a four-round programming competition.

**Features**
- Register teams (with duplicate team number checks), display, search and delete them
- Capture scores round by round; the lowest-scoring active team is eliminated each round
- Winner is the active team with the highest total score
- Input validation for empty fields and non-numeric or negative scores

**Data structure:** a hand-written singly linked list (`TeamNode`, `TeamLinkedList`), with no use of `java.util.LinkedList`.

## What this shows

- Implementing arrays, linked lists, searching and sorting without library shortcuts
- Separating data-structure logic from the GUI layer
- Custom Swing components (rounded buttons, panels, borders, themed tables)
- Input validation and clear user feedback

## How to run

Requires Java 8 or newer.

```
cd mobile-money
javac MobileMoneyGUI.java
java MobileMoneyGUI
```

```
cd competition-menu
javac CompetitionMenu.java
java CompetitionMenu
```

`transactions.txt` must be in the same folder you run the Mobile Money app from.

> Each project is kept in its own folder because both define their own `Theme`, `RoundedButton` and `RoundedPanel` classes, which would clash if compiled together.

## Folder structure

```
java-projects/
├── README.md
├── mobile-money/
│   ├── MobileMoneyGUI.java
│   └── transactions.txt
└── competition-menu/
    └── CompetitionMenu.java
```

## Screenshots

<!-- Add after uploading: ![Mobile Money](mobile-money/screenshot.png) -->
<!-- ![Competition Manager](competition-menu/screenshot.png) -->

## Author

**Sangulukani Musukwa**, Computer Science student, University of Lusaka, Zambia
