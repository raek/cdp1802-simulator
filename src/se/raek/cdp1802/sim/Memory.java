package se.raek.cdp1802.sim;

public interface Memory {

	int read(int addr);

	void write(int addr, int data);

}
