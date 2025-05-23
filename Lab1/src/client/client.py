import socket
import threading


class Client:
    def __init__(self, host=socket.gethostbyname(socket.gethostname()), port=12346, header=64, format="utf-8") -> None:
        self.host: str = host
        self.port: int = port
        self.client: socket.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.header: int = header
        self.format: str = format
        self.running: bool = True

    def start(self) -> None:
        try:
            self.client.connect((self.host, self.port))
            print(f"Connected to {self.host}:{self.port}")

            # Start a thread to listen for messages from the server
            threading.Thread(target=self.receive_messages, daemon=True).start()

            self.send("Hi from client!")

            # Main thread for sending messages
            while self.running:
                command = input("Enter command (e.g., light on, door lock, status)\nType 'help' for a list of commands.: ")

                valid_commands = ["light on", "light off", "door lock", "door unlock", "window open", "window close", "status", "exit", "help"]
                if command not in valid_commands:
                    print("Invalid command! Please try again.")
                    continue
                if command == "help":
                    print("Commands: light on, light off, door lock, door unlock, window open, window close, status, exit")
                    continue    
                if command == "exit":
                    self.running = False
                self.send(command)
        except Exception as e:
            print(f"Error: {e}")
        finally:
            self.client.close()
            print("Disconnected from server.")

    def send(self, msg) -> None:
        try:
            message = msg.encode(self.format)
            msg_length = len(message)
            send_length = str(msg_length).encode(self.format)
            send_length += b" " * (self.header - len(send_length))
            self.client.send(send_length)
            self.client.send(message)
        except BrokenPipeError:
            print("Connection to the server is broken.")

    def receive_messages(self) -> None:
        try:
            while self.running:
                message = self.client.recv(2048).decode(self.format)
                if message:
                    print(f"\nServer: {message}")
                else:
                    print("\nDisconnected from server.")
                    self.running = False
                    break
        except ConnectionResetError:
            print("Connection reset by server.")
        except OSError:
            pass


if __name__ == "__main__":
    client = Client()
    client.start()
