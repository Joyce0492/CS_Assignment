import random
import math


class Stack:
    """A simple stack data structure using a list as the underlying container."""

    def __init__(self):
        self._items = []

    def push(self, item):
        """Push an item onto the top of the stack."""
        self._items.append(item)

    def pop(self):
        """Remove and return the top item. Raises IndexError if empty."""
        if self.is_empty():
            raise IndexError("pop from empty stack")
        return self._items.pop()

    def peek(self):
        """Return the top item without removing it."""
        if self.is_empty():
            raise IndexError("peek from empty stack")
        return self._items[-1]

    def is_empty(self):
        """Return True if the stack has no items."""
        return len(self._items) == 0

    def size(self):
        """Return the number of items in the stack."""
        return len(self._items)


class Treasure:
    """Represents a treasure with position coordinates and value based on distance from origin."""

    def __init__(self, x=0, y=0):
        """
        Initialize a treasure at given coordinates.
        Args:
            x (int): x-coordinate of treasure
            y (int): y-coordinate of treasure
        """
        self.x = x
        self.y = y
        # Cache distance so it is only computed once
        self._distance = math.sqrt(x * x + y * y)

    def distance_from_origin(self):
        """Return the Euclidean distance from origin (0, 0)."""
        return self._distance

    def calculate_value(self):
        """
        Return treasure value based on distance from origin:
        - ≤ 2 units  : 500 points
        - 2–5 units  : 200 points
        - 5–10 units :  50 points
        - > 10 units :   0 points
        """
        d = self._distance
        if d <= 2:
            return 500
        elif d <= 5:
            return 200
        elif d <= 10:
            return 50
        return 0

    def __str__(self):
        return f"Treasure at ({self.x}, {self.y}) with value: {self.calculate_value()}"


class Hunter:
    """Represents a player who collects treasures."""

    def __init__(self, name="Unknown"):
        """
        Initialize a hunter with given name and empty treasure collection.
        Args:
            name (str): Hunter's name, defaults to "Unknown"
        """
        self.name = name
        self._total = 0  # Running total avoids repeated sum() calls

    def collect_treasure(self, treasure):
        """
        Add a treasure's value to the hunter's total.
        Args:
            treasure (Treasure): Treasure object to collect
        Returns:
            bool: True if total value exceeds 1000, False otherwise
        """
        value = treasure.calculate_value()
        self._total += value
        print(f"Hunter {self.name} collected treasure with value: {value}")
        if self._total > 1000:
            print(f"{self.name} has collected over 1000 points!")
            return True
        return False

    def get_total_value(self):
        """Return total value of all collected treasures."""
        return self._total

    def __str__(self):
        return f"Hunter {self.name} has collected treasures worth: {self._total}"


class TreasureMap:
    """Manages the game state and coordinates gameplay between two hunters."""

    def __init__(self, name1="Player 1", name2="Player 2", max_treasures=10):
        """
        Initialize game with two hunters and generate treasures.
        Args:
            name1 (str): First hunter's name
            name2 (str): Second hunter's name
            max_treasures (int): Number of treasures to generate
        """
        self.hunter1 = Hunter(name1)
        self.hunter2 = Hunter(name2)
        self.treasures = Stack()
        self.generate_treasures(max_treasures)

    def generate_treasures(self, max_treasures):
        """
        Generate random treasures within the -10 to 10 coordinate range.
        Args:
            max_treasures (int): Number of treasures to generate
        """
        for _ in range(max_treasures):
            x = random.randrange(-10, 11)
            y = random.randrange(-10, 11)
            self.treasures.push(Treasure(x, y))

    def start_hunt(self):
        """
        Run the treasure hunt, alternating turns between hunters.
        Returns:
            bool: True if a hunter exceeds 1000 points, False otherwise
        """
        round_number = 1
        while self.treasures.size() >= 2:
            print(f"--- Round {round_number} ---")

            # Hunter 1's turn
            if self.hunter1.collect_treasure(self.treasures.pop()):
                return True

            # Hunter 2's turn
            if self.hunter2.collect_treasure(self.treasures.pop()):
                return True

            round_number += 1
        return False

    def announce_winner(self):
        """Compare scores and announce the winner."""
        score1 = self.hunter1.get_total_value()
        score2 = self.hunter2.get_total_value()
        print("\n--- Final Scores ---")
        print(self.hunter1)
        print(self.hunter2)
        if score1 > score2:
            print(f"Congratulations! The winner is {self.hunter1.name} with {score1} points.")
        elif score2 > score1:
            print(f"Congratulations! The winner is {self.hunter2.name} with {score2} points.")
        else:
            print(f"It's a tie! Both hunters collected {score1} points.")


# ---------------------------------------------------------------------------
# Input validation helpers
# ---------------------------------------------------------------------------

def validate_max_treasures():
    """Prompt until the user enters a valid treasure count (1–50)."""
    while True:
        try:
            value = int(input("Enter the maximum number of treasures each pass (1-50): "))
            if 1 <= value <= 50:
                return value
            print("Please enter a number between 1 and 50.")
        except ValueError:
            print("Invalid input. Please enter a whole number.")


def validate_hunter_name(hunter_number):
    """Prompt until the user enters a non-empty hunter name."""
    ordinals = {1: "first", 2: "second"}
    label = ordinals.get(hunter_number, str(hunter_number))
    while True:
        name = input(f"Enter the name of the {label} hunter: ").strip()
        if name:
            return name
        print("Error: Name cannot be empty.")


def validate_continue_game():
    """Ask whether the players want another round. Returns 1 (yes) or 0 (no)."""
    while True:
        try:
            choice = int(input("Try again? Enter 1 for Yes or 0 for No: "))
            if choice in (0, 1):
                return choice
            print("Please enter 0 or 1.")
        except ValueError:
            print("Invalid input. Please enter 0 or 1.")


# ---------------------------------------------------------------------------
# Entry point
# ---------------------------------------------------------------------------

def main():
    """Main game loop."""
    print("Welcome to the Treasure Hunt Game!")

    max_treasures = validate_max_treasures()
    hunter1_name = validate_hunter_name(1)
    hunter2_name = validate_hunter_name(2)

    # Hunters persist across rounds so scores accumulate
    hunter1 = Hunter(hunter1_name)
    hunter2 = Hunter(hunter2_name)

    while True:
        game = TreasureMap(hunter1_name, hunter2_name, max_treasures)
        game.hunter1 = hunter1
        game.hunter2 = hunter2

        if game.start_hunt():
            break

        if validate_continue_game() == 0:
            break

    game.announce_winner()


if __name__ == "__main__":
    main()
