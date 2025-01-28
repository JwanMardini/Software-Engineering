import socket
import threading
import json
from house import House


class Server:
    def __init__(self, host=socket.gethostbyname(socket.gethostname()), port=12346, header=64, format="utf-8") -> None:
        self.host: str = host
        self.port: int = port
        self.server: socket.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.clients: list = []
        self.header: int = header
        self.format: str = format
        self.house: House = House()

    def start(self) -> None:
        self.server.bind((self.host, self.port))
        self.server.listen()
        print(f"Server started at {self.host}:{self.port}")
        while True:
            client, addr = self.server.accept()
            print(f"Client with address {addr} connected.")
            self.clients.append(client)
            print(f"Number of clients: {len(self.clients)}")
            threading.Thread(target=self.handle_client, args=(client, addr)).start()

    def handle_client(self, client, addr) -> None:
        client.send("Hi from server!\n".encode(self.format))
        connected = True
        while connected:
            try:
                msg_length = client.recv(self.header).decode(self.format)
                if msg_length:
                    msg_length = int(msg_length)
                    msg = client.recv(msg_length).decode(self.format)
                    if msg == "Hi from client!":
                        client.send(f"Welcome client {self.clients.index(client) + 1}!".encode(self.format))
                    elif msg == "exit":
                        print(f"Client {addr} disconnected.")
                        connected = False
                        self.clients.remove(client)
                        print(f"Number of clients: {len(self.clients)}")
                    else:
                        response = self.process_command(msg)
                        client.send(response.encode(self.format))
            except ConnectionResetError:
                print(f"Client {addr} forcibly closed the connection.")
                connected = False
                self.clients.remove(client)
                print(f"Number of clients: {len(self.clients)}")
        client.close()

    def process_command(self, command) -> str:
        parts = command.split()
        if len(parts) < 2:
            if command.lower() == "status":
                status = self.house.get_status_with_graph()
                return json.dumps(status, indent=2)
            return "Invalid command format. Use: <component> <action>."

        component, action = parts[0].lower(), parts[1].lower()

        if component == "light":
            self.house.change_light(action)
        elif component == "door":
            self.house.change_door(action)
        elif component == "window":
            self.house.change_window(action)
        else:
            return f"Unknown component '{component}'. Valid components: light, door, window, status."

        return f"{component.capitalize()} changed to {action}."


if __name__ == "__main__":
    server = Server()
    server.start()
