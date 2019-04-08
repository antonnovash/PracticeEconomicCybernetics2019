import javax.swing.*;

public class Add extends JFrame {

    private Catalog frame;
    private JLabel typeLabel;
    private JTextField typeTextField;
    private JButton closeButton;
    private JLabel numberLabel;
    private JTextField numberTextField;
    private JSeparator jSeparator1;
    private JSeparator jSeparator2;
    private JLabel colorLabel;
    private JTextField colorTextField;
    private JButton okButton;
    private JLabel speciesLabel;
    private JTextField speciesTextField;

    public Add(Catalog temp) {

        frame = temp;
        initComponents();
        this.setTitle("Add drag");
        this.setBounds(400, 200, 400, 150);
        this.setSize(350, 250);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    private void initComponents() {

        typeLabel = new JLabel();
        speciesLabel = new JLabel();
        colorLabel = new JLabel();
        typeTextField = new JTextField();
        speciesTextField = new JTextField();
        colorTextField = new JTextField();
        numberTextField = new JTextField();
        numberLabel = new JLabel();
        jSeparator1 = new JSeparator();
        jSeparator2 = new JSeparator();
        okButton = new JButton();
        closeButton = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        typeLabel.setText("Type of drag: ");
        speciesLabel.setText("Name of drag: ");
        colorLabel.setText("Manufactorer: ");

        typeTextField.addCaretListener(this::colorTextFieldCaretUpdate);

        speciesTextField.addCaretListener(this::speciesTextFieldCaretUpdate);
        colorTextField.addCaretListener(this::typeTextFieldCaretUpdate);
        numberLabel.setText("Number of drag:");
        okButton.setText("Add");
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okButtonMouseClicked(evt);
            }
        });
        closeButton.setText("Close");
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeButtonMouseClicked(evt);
            }
        });
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jSeparator1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(numberLabel)
                                                        .addComponent(colorLabel)
                                                        .addComponent(speciesLabel)
                                                        .addComponent(typeLabel)

                                                )
                                                .addGap(28, 28, 28)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(speciesTextField, GroupLayout.Alignment.TRAILING)
                                                        .addComponent(colorTextField, GroupLayout.Alignment.TRAILING)
                                                        .addComponent(numberTextField, GroupLayout.Alignment.TRAILING)

                                                        .addComponent(typeTextField)
                                                ))
                                        .addComponent(jSeparator2)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(okButton)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(closeButton))
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addGap(18, 18, 18)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED))
                                                        ))
                                                .addGap(0, 4, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(typeLabel)
                                        .addComponent(typeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(speciesLabel)
                                        .addComponent(speciesTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(colorLabel)
                                        .addComponent(colorTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(numberTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(numberLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(okButton)
                                        .addComponent(closeButton))
                                .addContainerGap())
        );
        pack();
    }

    private void colorTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_typeTextFieldCaretUpdate
        checkOkClose();
    }

    private void speciesTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_speciesTextFieldCaretUpdate
        checkOkClose();
    }

    private void typeTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_colorTextFieldCaretUpdate
        checkOkClose();
    }

    private void closeButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeButtonMouseClicked
        this.dispose();
    }

    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMouseClicked
        if (this.typeTextField.getText().isEmpty() || this.speciesTextField.getText().isEmpty()
                || this.colorTextField.getText().isEmpty() || this.numberTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Необходимо заполнить все поля!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (this.okButton.isEnabled()) {
            Catalog.addResult = new DragNode(
                    this.numberTextField.getText().isEmpty() ? "-" : this.numberTextField.getText(), this.typeTextField.getText(), this.speciesTextField.getText(), this.colorTextField.getText()
            );
            frame.addNewItem();
            this.dispose();
        }
    }

    public void checkOkClose() {

        if (!this.typeTextField.getText().isEmpty()
                && !this.speciesTextField.getText().isEmpty()
                && !this.colorTextField.getText().isEmpty()) {
            this.okButton.setEnabled(true);
        }
    }

    public void setKeyboardState(boolean flag) {

        this.typeTextField.setEditable(flag);
        this.speciesTextField.setEditable(flag);
        this.speciesTextField.setEditable(flag);
        this.colorTextField.setEditable(flag);
        this.numberTextField.setEditable(flag);
        this.okButton.setEnabled(false);
        this.closeButton.setEnabled(true);
        checkOkClose();
    }
}