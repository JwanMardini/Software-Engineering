# Lab 1: Smart House Client-Server Application

This project simulates controlling a smart home system via a client-server architecture. The client sends commands to the server to control house components like lights, doors, and windows. The server processes these commands, updates the components' states, and stores them in a JSON file for persistence.

Key Features:
- Command Processing: Control light, door, and window (e.g., "light on", "door lock").
- Status Check: Request real-time status with graphical representations (e.g., "light: 🟢 on").
- Persistent State: Component states are saved in house.json.
- Multithreading: Server handles multiple clients simultaneously, and the client listens for responses while interacting with the user.
- Command Validation: Ensures valid commands before sending them to the server.

The system allows users to interact with their smart home by sending commands and receiving updates on component states.

## Setup

To set up the environment, you will need to use a Python virtual environment (`venv`). The provided `Makefile` includes commands to create the environment, install dependencies, and run the server and client.

## Makefile Commands

- **Create Environment**: To create the virtual envirement, run:
    ```sh
    make venv
    ```


- **Install depandesies**: To install depandesies, run:
    ```sh
    make install
    ```

- **Run Server**: To start the server, run:
    ```sh
    make run_server
    ```

- **Run Client**: To start the client, run:
    ```sh
    make run_client
    ```


## Notes

- Ensure that the server is running before starting the client.
- Follow the sub-task instructions carefully to complete the lab.
