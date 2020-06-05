package notebook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	public static Logger logger = LoggerFactory.getLogger("NotebookController");
	public static void main(String[] args) {
		try {
			// Start Controllers
			logger.info("[Main] Init & start Notebook Controller");
			K8sApiCaller.initK8SClient();
			K8sApiCaller.startWatcher(); // Infinite loop
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}