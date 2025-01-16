import json
import os


class House:
    def __init__(self, path=os.path.join(os.path.dirname(__file__), "resources" + os.sep + "house.json")):
        self.light = "off"
        self.door = "locked"
        self.window = "open"
        self.path = path
        self.load()

    def load(self):
        try:
            with open(self.path, "r") as file:
                data = json.load(file)
                self.light = data.get("light", "off")
                self.door = data.get("door", "locked")
                self.window = data.get("window", "open")
        except FileNotFoundError:
            self.save()

    def save(self):
        data = {
            "light": self.light,
            "door": self.door,
            "window": self.window,
        }
        with open(self.path, "w") as file:
            json.dump(data, file)

    def change_light(self, status):
        self.light = status
        self.save()

    def change_door(self, status):
        self.door = status
        self.save()

    def change_window(self, status):
        self.window = status
        self.save()

    def get_graph(self, value):
        symbols = {
            "on": "🟢",
            "off": "🔴",
            "open": "🟩",
            "close": "🟥",
            "locked": "🔒",
            "unlocked": "🔓",
        }
        return symbols.get(value, "❓")

    def get_status(self):
        return {
            "light": self.light,
            "door": self.door,
            "window": self.window,
        }

    def get_status_with_graph(self):
        return {
            "light": f"{self.get_graph(self.light)} {self.light}",
            "door": f"{self.get_graph(self.door)} {self.door}",
            "window": f"{self.get_graph(self.window)} {self.window}",
        }
