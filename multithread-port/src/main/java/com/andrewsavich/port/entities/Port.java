package com.andrewsavich.port.entities;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Port {
	private String portName;
	private Semaphore pier;
	private int warehouseCapacity;
	private Warehouse warehouse;

	public Port(String portName, int quantityPiers, int warehouseCapacity) {
		this.portName = portName;
		this.setWarehouseCapacity(warehouseCapacity);
		pier = new Semaphore(quantityPiers, true);
		warehouse = new Warehouse(warehouseCapacity);
	}
	
	public Semaphore getPier() {
		return pier;
	}
	
	public String getPortName() {
		return portName;
	}
	
	public void setPortName(String portName) {
		this.portName = portName;
	}

	public void replaceContainerFromShipToWarehouse(Container container) {
		warehouse.putContainer(container);
	}
	
	public void replaceAllContainersFromShip(BlockingQueue<Container> containers) {
		warehouse.putContainers(containers);
	}
	
	public Container replaceContainerFromWarehouseToShip() throws InterruptedException {
		return warehouse.getContainer();
	}
	
	public int getQuantityContainers(){
		return warehouse.getQuantityContainers();
	}

	public int getWarehouseCapacity() {
		return warehouseCapacity;
	}

	public void setWarehouseCapacity(int warehouseCapacity) {
		this.warehouseCapacity = warehouseCapacity;
	}
	
	public Warehouse getWarehouse(){
		return warehouse;
	}

}