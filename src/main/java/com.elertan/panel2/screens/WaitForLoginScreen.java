package com.elertan.panel2.screens;

import com.elertan.BUResourceService;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;

public class WaitForLoginScreen extends JPanel {
    @Inject
    public WaitForLoginScreen(BUResourceService buResourceService) {
        setLayout(new BorderLayout());

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel titleLabel = new JLabel("Bronzeman Unleashed");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(titleLabel);

        JLabel subtitleLabel = new JLabel();
        subtitleLabel.setText("Waiting for account login...");
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(20, 15, 0, 15));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(subtitleLabel);

        inner.add(Box.createVerticalStrut(15));

        JLabel loadingSpinnerLabel = new JLabel();
        loadingSpinnerLabel.setIcon(buResourceService.getLoadingSpinnerImageIcon());
        loadingSpinnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(loadingSpinnerLabel);

        add(inner, BorderLayout.NORTH);
    }
}
