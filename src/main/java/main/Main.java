package main;

/**
 * Main entry point for Auto2i application.
 * 
 * This class provides a simple entry point that launches the GUI application.
 * For database setup, run DatabaseSetup first.
 * 
 * Usage:
 * 1. Run DatabaseSetup to initialize database (only once)
 * 2. Run Auto2iApplication (or this Main class) to start the GUI
 */
public class Main {
    public static void main(String[] args) {
        // Delegate to the Auto2iApplication launcher
        Auto2iApplication.main(args);
    }
}