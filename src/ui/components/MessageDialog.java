package ui.components;

import javax.swing.*;
import java.awt.*;

public class MessageDialog {

    // Modern color palette
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243); // Blue
    private static final Color SECONDARY_COLOR = new Color(255, 87, 34); // Orange
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Dark gray
    private static final Color BUTTON_HOVER_COLOR = new Color(25, 118, 210); // Darker blue

    static {
        // Global UI customizations for JOptionPane
        UIManager.put("OptionPane.background", BACKGROUND_COLOR); // Light gray background
        UIManager.put("Panel.background", BACKGROUND_COLOR); // Background of internal panel
        UIManager.put("OptionPane.messageFont", new Font("SansSerif", Font.BOLD, 16)); // Bold font
        UIManager.put("OptionPane.messageForeground", TEXT_COLOR); // Dark gray text
        UIManager.put("Button.background", PRIMARY_COLOR); // Blue button background
        UIManager.put("Button.foreground", Color.WHITE); // White button text
        UIManager.put("Button.border", BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Button padding
        UIManager.put("Button.focusPainted", false); // Remove focus border
        UIManager.put("Button.margin", new Insets(10, 20, 10, 20)); // Button margin
        UIManager.put("Button.focus", new Color(0, 0, 0, 0)); // Remove focus highlight
    }

    public static void showSuccessMessage(JFrame frame, String message) {
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        messageLabel.setForeground(new Color(25, 135, 84)); // Green success color

        JOptionPane.showMessageDialog(frame, messageLabel, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorMessage(JFrame frame, String message) {
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        messageLabel.setForeground(new Color(200, 0, 0)); // Red error color

        JOptionPane.showMessageDialog(frame, messageLabel, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarningMessage(JFrame frame, String message) {
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        messageLabel.setForeground(new Color(255, 165, 0)); // Orange warning color

        JOptionPane.showMessageDialog(frame, messageLabel, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}