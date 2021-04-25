package com.andrewsavich.port.entities;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ship extends Thread {
	private Semaphore currentPier;
	private Port currentPort;
	private String shipName;
	private int liftingCapacity;
	private BlockingQueue<Container> containers;

	private static final Logger LOGGER = LoggerFactory.getLogger(Ship.class);

	public Ship(String shipName, int liftingCapacity) {
		this.shipName = shipName;
		this.liftingCapacity = liftingCapacity;
		this.containers = new LinkedBlockingQueue<>();
	}

	public void setCurrentPort(Port currentPort) {
		this.currentPort = currentPort;
	}

	public void putContainer(Container container) {
		if (currentPier == null) {
			throw new IllegalStateException("Putting unposible! Because the ship isn't in port!");
		}

		if (containers.size() == liftingCapacity) {
			throw new IndexOutOfBoundsException("Ship loaded fully");
		}

		try {
			containers.put(container);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Container unloadContainer() throws InterruptedException {
		checkingBeforUnloadContainers();

		return containers.take();
	}

	public BlockingQueue<Container> unloadAllContainers() throws InterruptedException {
		checkingBeforUnloadContainers();

		int availablePlaces = currentPort.getWarehouse().getAvailablePlaces();
		BlockingQueue<Container> unloadingContainers;

		if (availablePlaces > containers.size()) {
			unloadingContainers = new LinkedBlockingQueue<>(containers);
			containers.clear();
			return unloadingContainers;
		}

		unloadingContainers = new LinkedBlockingQueue<>();

		for (int i = 0; i < availablePlaces; i++) {
			unloadingContainers.put(unloadContainer());
		}

		return unloadingContainers;

	}

	private void checkingBeforUnloadContainers() {
		if (currentPier == null) {
			throw new IllegalStateException("Unloading unposible! Because the ship isn't in port!");
		}

		if (containers.isEmpty()) {
			throw new IllegalStateException("Ship don't have any containers");
		}

		if (currentPort.getWarehouse().getAvailablePlaces() == 0) {
			throw new IllegalStateException(
					"Can't uload containers, because warehause of " + currentPort.getPortName() + " is full");
		}
	}

	public void showAllContainers() {
		if (containers.isEmpty()) {
			LOGGER.info("Ship don't have any containers");
		} else {
			LOGGER.info(shipName + " has " + getQuantityContainers() + " containers");
		}
	}

	public void generateLoadedContainers() {
		int quantityLoadedContainers = ThreadLocalRandom.current().nextInt(1, liftingCapacity);

		for (int i = 0; i < quantityLoadedContainers; i++) {
			containers.add(new Container());
		}

		LOGGER.info("For " + shipName + " was generated " + quantityLoadedContainers + " containers");
	}

	public int getQuantityContainers() {
		return containers.size();
	}

	public void sendToSea(Port port) {
		setCurrentPort(port);
		start();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(500);
			LOGGER.info(shipName + " sailed to " + currentPort.getPortName() + " and waiting for pier");
			currentPier = currentPort.getPier();
			currentPier.acquire();
			LOGGER.info(shipName + " got access to pier of " + currentPort.getPortName());
			currentPort.replaceAllContainersFromShip(unloadAllContainers());
			LOGGER.info(shipName + " unloaded containers and release pier");
			currentPier.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
