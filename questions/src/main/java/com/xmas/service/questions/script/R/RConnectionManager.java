package com.xmas.service.questions.script.R;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RConnectionManager {

    public static final String R_SERVE_HOST = "localhost";
    public static final int R_SERVE_PORT = 6311;

    @Value("${rserve.bin.path}")
    private String rserveHomeBin;

    private static final Logger logger = LogManager.getLogger();

    private RConnection connection;


    public synchronized RConnection getConnection() {
        if (connection != null && connection.isConnected()) {
            return connection;
        } else {
            return createConnection();
        }
    }

    private synchronized RConnection createConnection() {
        try {
            return connection = new RConnection(R_SERVE_HOST, R_SERVE_PORT);
        } catch (RserveException rse) {
            logger.warn("Can't connect to Rserve.Try to run new process.");
            startRserve();
            return createConnection();
        }
    }

    private synchronized void startRserve() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(("R CMD " + rserveHomeBin + " --no-save").split(" "));
            Process process = processBuilder.start();
            System.out.println(process.waitFor());
        } catch (InterruptedException e) {
            logger.error("Can't wait for R process.");
            logger.error(e.getMessage());
            logger.debug(e.getMessage(), e);
        } catch (IOException e) {
            logger.error("Can't start Rserve. Maybe it's not installed.");
            logger.error(e.getMessage());
            logger.debug(e.getMessage(), e);
        }
    }

    public String getRserveHomeBin() {
        return rserveHomeBin;
    }

    public void setRserveHomeBin(String rserveHomeBin) {
        this.rserveHomeBin = rserveHomeBin;
    }
}
