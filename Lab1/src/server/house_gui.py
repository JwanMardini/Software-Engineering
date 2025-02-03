import tkinter as tk
from house import House


class HouseGUI:
    def __init__(self, house: House) -> None:
        self.house = house
        self.root = tk.Tk()
        self.root.title("House State GUI")
        self.root.geometry("500x500")

        # Light (Circle)
        self.light_canvas = tk.Canvas(self.root, width=100, height=100)
        self.light_canvas.pack(pady=10)
        self.light_circle = self.light_canvas.create_oval(25, 25, 75, 75, fill="red")

        # Door (Triangle)
        self.door_canvas = tk.Canvas(self.root, width=100, height=100)
        self.door_canvas.pack(pady=10)
        self.door_triangle = self.door_canvas.create_polygon(50, 25, 25, 75, 75, 75, fill="blue")

        # Window (Square)
        self.window_canvas = tk.Canvas(self.root, width=100, height=100)
        self.window_canvas.pack(pady=10)
        self.window_square = self.window_canvas.create_rectangle(25, 25, 75, 75, fill="green")

        # Update GUI with initial state
        self.update_gui()

    def update_gui(self) -> None:
        """Update the GUI based on the current state of the house."""
        # Light
        light_color = "yellow" if self.house.light == "on" else "red"
        self.light_canvas.itemconfig(self.light_circle, fill=light_color)

        # Door
        door_color = "orange" if self.house.door == "unlock" else "blue"
        self.door_canvas.itemconfig(self.door_triangle, fill=door_color)

        # Window
        window_color = "white" if self.house.window == "open" else "green"
        self.window_canvas.itemconfig(self.window_square, fill=window_color)

    def run(self) -> None:
        """Start the Tkinter main loop."""
        self.root.mainloop()
