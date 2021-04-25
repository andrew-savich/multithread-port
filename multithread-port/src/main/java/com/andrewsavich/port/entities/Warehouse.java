package com.andrewsavich.port.entities;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Warehouse {
	private int maxContainers;
	private BlockingQueue<Container> containers;
	
	public Warehouse(int maxContainers) {
		this.maxContainers = maxContainers;
		containers = new LinkedBlockingQueue<>();
	}

	public void putContainer(Container container) {
		if (getAvailablePlaces() == 0) {
			throw new IndexOutOfBoundsException("Warehouse is full");
		}
		
		try {
			containers.put(container);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void putContainers(BlockingQueue<Container> containers) {
		
		for(Container container : containers) {
			if (getAvailablePlaces() == 0) {
				throw new IndexOutOfBoundsException("Warehouse is full");
			}
			putContainer(container);
		}
	}
	
	public Container getContainer() throws InterruptedException {
		if(containers.isEmpty()) {
			throw new IllegalStateException("Warehouse is empty");
		}
		
		return containers.take(); 
	}
	
	public int getAvailablePlaces() {
		return maxContainers - containers.size();
	}
	
	public int getQuantityContainers() {
		if(containers.isEmpty()) {
			return 0;
		}else {
			return containers.size();
		}
	}

}
