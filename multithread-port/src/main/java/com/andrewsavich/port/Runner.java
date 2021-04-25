package com.andrewsavich.port;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andrewsavich.port.entities.Port;
import com.andrewsavich.port.entities.Ship;

public class Runner {

	private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

	public static void main(String[] args) {
		Port port = new Port("FirstPORT", 3, 20);
		List<Ship> ships = new ArrayList<>();

		LOGGER.info("Creating ships");
		for (int i = 0; i < 10; i++) {
			Ship ship = new Ship("Ship " + i, 5);
			ship.generateLoadedContainers();

			ships.add(ship);
		}

		for (Ship ship : ships) {
			ship.sendToSea(port);
			try {
				ship.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		LOGGER.info(port.getPortName() + " has "+ port.getQuantityContainers() + " containers");
		
		for (Ship ship : ships) {
			LOGGER.info(ship.getName() + " has " + ship.getQuantityContainers());
		}

	}
}
