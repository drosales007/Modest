package modest;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.FileWriter;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import java.util.Map;
import javax.swing.text.DefaultCaret;
import static java.lang.Math.abs;
import static java.lang.Math.round;
import static java.lang.Math.abs;
import static java.lang.Math.round;

public class Modest extends javax.swing.JFrame {
    
    static final HashMap<String, String> MAP;
    static String MSG_TXT = "";
    static MessagingQueue mqueue = new MessagingQueue();
    static Boolean[] ThreadStatus;
    static Boolean messagingEnabled = true;
    
    static HashMap<Integer, Connector> Connectors = new HashMap<>();
    static HashMap<Integer, ServerLabel> srvLabels = new HashMap<>();
    static HashMap<Integer, UILinker> srvLinkers = new HashMap<>();
    HashMap<Integer, UIServerThread> srvThreads = new HashMap<>();
    
    static CSLabel csLabel;
    static JLayeredPane jlp;
    
    UIMsgQueueThread qThread;
    UINameServerThread nsThread;
    
    static ExecutorService threadPool;
    
    boolean running = false;
    boolean guiSet = false;
    
    static int iterations = 5;
    static String algorithm = "Lamport Mutex";
    static String custom = "False";
    static boolean automated = true;
    static int msgLifespan = 1;

    static {
        MAP = new HashMap<String, String>();
        MAP.put("Lamport Mutex", "LamportMutex");
        MAP.put("Ricart-Argawala", "RAMutex");
        MAP.put("Dining Philosopher", "DiningPhil");
        MAP.put("Token-Based", "CircToken");
        MAP.put("Lamport Bakery", "LamportBakery");
    }

    /**
     * Creates new JFrame
     */
    public Modest() {
        initComponents();
        initGUI(Integer.parseInt(serversCombo.getSelectedItem().toString()));
        this.jlp = layeredPane;
        this.threadPool = Executors.newFixedThreadPool(200);
    }
    
    @SuppressWarnings("unchecked")                         
    private void initComponents() {
        // Set up the GUI
        
        msgRadioGroup = new javax.swing.ButtonGroup();
        simRadioGroup = new javax.swing.ButtonGroup();
        Frame = new javax.swing.JPanel();
        srvCtlPanel = new javax.swing.JPanel();
        configPanel = new javax.swing.JPanel();
        algorithmLabel = new javax.swing.JLabel();
        algorithmCombo = new javax.swing.JComboBox<>();
        serversLabel = new javax.swing.JLabel();
        serversCombo = new javax.swing.JComboBox<>();
        startButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        iterationsLabel = new javax.swing.JLabel();
        iterationCombo = new javax.swing.JComboBox<>();
        String[] vals = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "<html>&infin;</html>"};
        iterationCombo.setModel(new javax.swing.DefaultComboBoxModel<>(vals));
        iterationCombo.setSelectedIndex(4);
        simulationLabel = new javax.swing.JLabel();
        simRadioAuto = new javax.swing.JRadioButton();
        simRadioManual = new javax.swing.JRadioButton();
        msgPanel = new javax.swing.JPanel();
        msgRadioOn = new javax.swing.JRadioButton();
        msgRadioOff = new javax.swing.JRadioButton();
        lifespanLabel = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        lsCombo = new javax.swing.JComboBox<>();
        animationPanel = new javax.swing.JPanel();
        layeredPane = new javax.swing.JLayeredPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        DefaultCaret caret = (DefaultCaret) jTextPane1.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Frame.setBackground(new java.awt.Color(102, 102, 102));
        Frame.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        srvCtlPanel.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout srvCtlPanelLayout = new javax.swing.GroupLayout(srvCtlPanel);
        srvCtlPanel.setLayout(srvCtlPanelLayout);
        srvCtlPanelLayout.setHorizontalGroup(
            srvCtlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        srvCtlPanelLayout.setVerticalGroup(
            srvCtlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1019, Short.MAX_VALUE)
        );

        configPanel.setBackground(new java.awt.Color(153, 153, 153));
        configPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        algorithmLabel.setText("Algorithm");

        algorithmCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"Lamport Mutex", "Ricart-Argawala", "Lamport Bakery", "Custom" }));
        algorithmCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                algorithmComboActionPerformed(evt);
            }
        });

        serversLabel.setText("Servers");

        serversCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"3", "4", "5", "6", "7", "8" }));
        serversCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serversComboActionPerformed(evt);
            }
        });

        startButton.setBackground(new java.awt.Color(153, 153, 153));
        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        resetButton.setBackground(new java.awt.Color(153, 153, 153));
        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        iterationsLabel.setText("Iterations");

        iterationCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iterationComboActionPerformed(evt);
            }
        });

        simulationLabel.setText("Simulation");

        simRadioAuto.setBackground(new java.awt.Color(153, 153, 153));
        simRadioGroup.add(simRadioAuto);
        simRadioAuto.setSelected(true);
        simRadioAuto.setText("Auto");
        simRadioAuto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simRadioAutoActionPerformed(evt);
            }
        });

        simRadioManual.setBackground(new java.awt.Color(153, 153, 153));
        simRadioGroup.add(simRadioManual);
        simRadioManual.setText("Manual");
        simRadioManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simRadioManualActionPerformed(evt);
            }
        });

        msgPanel.setBackground(new java.awt.Color(153, 153, 153));
        msgPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Messaging"));

        msgRadioOn.setBackground(new java.awt.Color(153, 153, 153));
        msgRadioGroup.add(msgRadioOn);
        msgRadioOn.setSelected(true);
        msgRadioOn.setText("On");
        msgRadioOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                msgRadioOnActionPerformed(evt);
            }
        });

        msgRadioOff.setBackground(new java.awt.Color(153, 153, 153));
        msgRadioGroup.add(msgRadioOff);
        msgRadioOff.setText("Off");
        msgRadioOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                msgRadioOffActionPerformed(evt);
            }
        });

        lifespanLabel.setText("Lifespan (s)");

        saveButton.setBackground(new java.awt.Color(153, 153, 153));
        saveButton.setText("Save Log");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        lsCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"1", "2", "3", "4", "5" }));
        lsCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lsComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout msgPanelLayout = new javax.swing.GroupLayout(msgPanel);
        msgPanel.setLayout(msgPanelLayout);
        msgPanelLayout.setHorizontalGroup(
            msgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(msgPanelLayout.createSequentialGroup()
                .addGroup(msgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(msgRadioOn)
                    .addComponent(msgRadioOff))
                .addGap(71, 71, 71)
                .addGroup(msgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(msgPanelLayout.createSequentialGroup()
                        .addComponent(lifespanLabel)
                        .addContainerGap())
                    .addGroup(msgPanelLayout.createSequentialGroup()
                        .addComponent(lsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addComponent(saveButton))))
        );
        msgPanelLayout.setVerticalGroup(
            msgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(msgPanelLayout.createSequentialGroup()
                .addComponent(msgRadioOn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(msgRadioOff)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, msgPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(saveButton))
            .addGroup(msgPanelLayout.createSequentialGroup()
                .addComponent(lifespanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout configPanelLayout = new javax.swing.GroupLayout(configPanel);
        configPanel.setLayout(configPanelLayout);
        configPanelLayout.setHorizontalGroup(
            configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, configPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(configPanelLayout.createSequentialGroup()
                        .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(algorithmLabel)
                            .addComponent(algorithmCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(serversLabel)
                            .addComponent(serversCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(startButton))
                .addGap(18, 18, 18)
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(iterationsLabel)
                    .addComponent(iterationCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resetButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(simulationLabel)
                    .addComponent(simRadioManual)
                    .addComponent(simRadioAuto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 559, Short.MAX_VALUE)
                .addComponent(msgPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        configPanelLayout.setVerticalGroup(
            configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configPanelLayout.createSequentialGroup()
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(configPanelLayout.createSequentialGroup()
                        .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(algorithmLabel)
                            .addComponent(serversLabel)
                            .addComponent(iterationsLabel)
                            .addComponent(simulationLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(algorithmCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(serversCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(iterationCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, configPanelLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(simRadioAuto)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 13, Short.MAX_VALUE)
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton)
                    .addComponent(resetButton)
                    .addComponent(simRadioManual)))
            .addComponent(msgPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        animationPanel.setBackground(new java.awt.Color(255, 255, 255));
        animationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Simulator"));
        setSimTitle(algorithmCombo.getSelectedItem().toString().trim());

        javax.swing.GroupLayout layeredPaneLayout = new javax.swing.GroupLayout(layeredPane);
        layeredPane.setLayout(layeredPaneLayout);
        layeredPaneLayout.setHorizontalGroup(
            layeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
        );
        layeredPaneLayout.setVerticalGroup(
            layeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout animationPanelLayout = new javax.swing.GroupLayout(animationPanel);
        animationPanel.setLayout(animationPanelLayout);
        animationPanelLayout.setHorizontalGroup(
            animationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPane)
        );
        animationPanelLayout.setVerticalGroup(
            animationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPane)
        );

        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.getVerticalScrollBar().addAdjustmentListener(
            new AdjustmentListener() {public void 
                adjustmentValueChanged(AdjustmentEvent e) {         
                    if (e.getValueIsAdjusting()){
                        e.getAdjustable().setValue(
                            e.getAdjustable().getValue());
                    } else {
                        e.getAdjustable().setValue(
                            e.getAdjustable().getMaximum());
                    }
                }
            }
        );

        jTextPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Log"));
        jScrollPane1.setViewportView(jTextPane1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 902, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout FrameLayout = new javax.swing.GroupLayout(Frame);
        Frame.setLayout(FrameLayout);
        FrameLayout.setHorizontalGroup(
            FrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FrameLayout.createSequentialGroup()
                .addComponent(srvCtlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(FrameLayout.createSequentialGroup()
                        .addComponent(animationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(configPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        FrameLayout.setVerticalGroup(
            FrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(srvCtlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(FrameLayout.createSequentialGroup()
                .addComponent(configPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(animationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Frame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Frame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // Clean up anything that may have been left over from a previous run
        if (running){
            cleanUp();
        }
        running = true;
        String alg = "";
        
        // Perform the import if selected
        if (algorithm.equals("Custom")){
                final JFileChooser fc = new JFileChooser();
                int ret = fc.showOpenDialog(fc);
                if (ret==JFileChooser.APPROVE_OPTION){
                    algorithm = fc.getSelectedFile().getAbsolutePath();
                    custom = "True";
                }
        }
        
        if (custom.equals("True")){
            System.out.println("Custom Import");
            alg = algorithm;
            algorithm = "Custom";
        } else {
            algorithm = algorithmCombo.getSelectedItem().toString().trim();
            alg = MAP.get(algorithm);
            algorithm = alg;
            custom = "False";
        }
        
        if (alg.length() > 0){
            int nodes = Integer.parseInt(
                    serversCombo.getSelectedItem().toString());
            
            // Handle starting simulation while one is currently running
            if (!guiSet){
                initGUI(nodes);
            }
            if (guiSet && running){
                cleanUp();
                clearGUI();
                initGUI(nodes);
            }
        
            // This thread will handle message queuing and the UI display
            qThread = new UIMsgQueueThread(jTextPane1, jScrollPane1);
            threadPool.execute(qThread);

            // Start The UI NameServer thread
            // This thread will handle starting the Nameserver
            nsThread = new UINameServerThread();
            threadPool.execute(nsThread);
            
            String msg = "Starting Run\n\n"
                         + "Algorithm: " + algorithm + "\n"
                         + "# of Servers:" + nodes + "\n"
                         + "Iterations: " + iterations + "\n\n\n";
            mqueue.enqueue(msg);
        
            // Start the UI Server Threads to handle starting each server thread
            for(int x=0; x<nodes; x++){
                String base_name = algorithm + "_test";
                String id = Integer.toString(x);
                String[] args = {base_name, id, Integer.toString(nodes), alg,
                                 custom};
                UIServerThread srvThread = new UIServerThread(args);
                srvThreads.put(Integer.parseInt(id), srvThread);
                threadPool.execute(srvThread);
            }
        }
    }                                           

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // Wait for all messages to clear
        mqueue.clear();
        
        // Clean up the GUI
        running = false;
        cleanUp();
        clearGUI();
        guiSet = false;
        algorithm = algorithmCombo.getSelectedItem().toString().trim();
    }                                           

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // handle saving the log file
        String txt = jTextPane1.getText();
        JFileChooser chooser = new JFileChooser();
        int retrival = chooser.showSaveDialog(null);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            try {
                String fil = chooser.getSelectedFile() + ".txt";
                FileWriter fw = new FileWriter(fil);
                fw.write(txt);
                fw.close();
            } catch (Exception exc) {
                System.err.println(exc);
                exc.printStackTrace();
            }
        }
    }                                          

    private void msgRadioOnActionPerformed(java.awt.event.ActionEvent evt) {                                           
        messagingEnabled = true;
    }                                          

    private void msgRadioOffActionPerformed(java.awt.event.ActionEvent evt) {                                            
        messagingEnabled = false;
    }                                           

    private void serversComboActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // Sets up the UI when the number of nodes have been chosen
        if (!running){
            clearGUI();
            int nodes = Integer.parseInt(serversCombo.getSelectedItem().toString());
            initGUI(nodes);
        }
    }                                            

    private void iterationComboActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // Get the number of iterations
        String val = iterationCombo.getSelectedItem().toString();
        if (val.equals("<html>&infin;</html>")){
            iterations = Integer.MAX_VALUE;
        } else {
            iterations = Integer.parseInt(val);
        }
    }                                              

    private void algorithmComboActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // Get the algorithm selected
        algorithm = algorithmCombo.getSelectedItem().toString().trim();
        if (!running && algorithm.equals("Custom")){
            //Create a file chooser
            final JFileChooser fc = new JFileChooser();
            int ret = fc.showOpenDialog(fc);
            if (ret==JFileChooser.APPROVE_OPTION){
                algorithm = fc.getSelectedFile().getAbsolutePath();
                custom = "True";
            }  
        } else {
            custom = "False";
        }
        setSimTitle(algorithm);
    }                                              

    private void simRadioAutoActionPerformed(java.awt.event.ActionEvent evt) {                                             
        automated = true;
    }                                            

    private void simRadioManualActionPerformed(java.awt.event.ActionEvent evt) {                                               
        automated = false;
    }                                              

    private void lsComboActionPerformed(java.awt.event.ActionEvent evt) {                                        
        msgLifespan = Integer.parseInt(
                lsCombo.getSelectedItem().toString().trim());
    }                                       

    public void initGUI(int num_srvs){
        // Creates the simulation GUI environment
        guiSet = true;
        displayServers(num_srvs);
        displayCS(num_srvs);
        displayServerControls(num_srvs);
    }
    
    public void setSimTitle(String alg){
        if (!running){
            animationPanel.setBorder(javax.swing.BorderFactory.
                createTitledBorder(null, alg, javax.swing.border.
                TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.
                TitledBorder.DEFAULT_POSITION, null, java.awt.Color.black));
        }
    }
    
    public void displayServers(int num_srvs){
        // Displays the servers in the animation panel
        int centerX = layeredPane.getWidth()/2;
        int centerY = layeredPane.getHeight()/2;
        int imgW = 64;
        int imgH = 64;
        
        String img_name;
        ImageIcon icon;
        
        Point[] pts = getLocations(num_srvs, centerX*2, centerY*2);
        
        // Add a JLabel for each server
        for (int x = 1; x <= num_srvs; x++){
            img_name = "server" + x + ".png";
            icon = new ImageIcon(getClass().getResource(img_name));
            ServerLabel lbl = new ServerLabel(icon, x-1, imgW, imgH, pts[x-1]);
            layeredPane.add(lbl);
            srvLabels.put((x-1), lbl);
        }
    }
    
    public void displayCS(int num_srvs){
        // Displays critical section in animation panel
        int centerX = layeredPane.getWidth()/2;
        int centerY = layeredPane.getHeight()/2;
        int imgW = 200;
        int imgH = 200;
        
        String img_name = "critical_section.png";
        ImageIcon icon = new ImageIcon(getClass().getResource(img_name));
        Point coords = new Point(centerX, centerY);
        csLabel = new CSLabel(icon, imgW, imgH, coords, num_srvs);
        csLabel.setBounds(csLabel.x, csLabel.y, csLabel.height, csLabel.width);
        layeredPane.add(csLabel);
    }
    
    public void displayServerControls(int num_srvs){
        // Displays the Server Control Panels for each server
        GroupLayout jPanel2Layout = new javax.swing.GroupLayout(srvCtlPanel);
        srvCtlPanel.setLayout(jPanel2Layout);
        
        GroupLayout.ParallelGroup parGroup1 = jPanel2Layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING);
        GroupLayout.ParallelGroup parGroup2 = jPanel2Layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING);
        GroupLayout.SequentialGroup seqGroup = 
                jPanel2Layout.createSequentialGroup();
        
        for (int x=0; x<num_srvs; x++){
            
            ServerControlPanel scp = new ServerControlPanel(layeredPane, srvLabels.get(x));
            scp.setBackground(new java.awt.Color(153, 153, 153));
            scp.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
            parGroup1.addComponent(scp, javax.swing.GroupLayout.DEFAULT_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE,
                              Short.MAX_VALUE);
            seqGroup.addComponent(scp, javax.swing.GroupLayout.PREFERRED_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE,
                              javax.swing.GroupLayout.PREFERRED_SIZE);
            seqGroup.addPreferredGap(
                javax.swing.LayoutStyle.ComponentPlacement.RELATED);
        }
        
        jPanel2Layout.setHorizontalGroup(parGroup1);
        parGroup2.addGroup(seqGroup);
        jPanel2Layout.setVerticalGroup(parGroup2);
    }
    
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Modest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Modest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Modest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Modest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Modest().setVisible(true);
            }
        });
    }

    public void cleanUp(){
        // Method for cleaning up past runs

        // Close any sockets that might still be open
        closeOpenSockets();
        
        // Close server communication links
        closeLinkers();

        // Close any SwingWorker threads we may have created
        closeWorkers();
        
        // Clear the queue
        mqueue.clear();
        
        // Clear the logging pane
        jTextPane1.setText("");
    }
    
    public void closeOpenSockets(){
        // Close server connections
        if (Connectors.size() > 0){
            Iterator itr = Connectors.entrySet().iterator();
            while (itr.hasNext()){
                Map.Entry pair = (Map.Entry)itr.next();
                Connector conn = (Connector) pair.getValue();
                conn.closeSockets();
            }
            Connectors.clear();
        }
    }
    
    public void closeWorkers(){
        // Kill long running SwingWorkers
        if (srvThreads.size() > 0){
            qThread.cancel(true);
            nsThread.cancel(true);
            srvThreads.clear();
        }
    }
    
    public void closeLinkers(){
        // Clear Linker references
        if (srvLinkers.size() > 0){
            srvLinkers.clear();
        }
    }
    
    public void clearSrvLabels(){
        // Clear ServerLabel references
        if (srvLabels.size() > 0){
            srvLabels.clear();
        }
    }
    
    public void clearGUI(){
        // Cleans the GUI
        srvCtlPanel.removeAll();
        srvCtlPanel.repaint();
        layeredPane.removeAll();
        layeredPane.repaint();
        clearSrvLabels();
    }
    
    public Point[] getLocations(int num_srvs, int w, int h){
        // Finds the locations where each server should be displayed
        int deg_split = 360/num_srvs;
        int centerX = w/2;
        int centerY = h/2;
        int offset = 50;
        int radius = h/2 - offset;
        int point = 360;
        double theta = 0;
        int hyp = radius;
        int opp;
        int adj;
        int x;
        int y;
        int quad;
        
        // 2-dimensional array to store x and y coordinates
        Point[] locs = new Point[num_srvs];
        
        // We will always set the first server in the same position
        locs[0] = new Point(centerX, centerY - hyp);
        
        // We will use some trigonometry to determine our coordinates
        for (int z=1; z<num_srvs; z++){
            // Get the angle relataive to the quadrant
            point = point - deg_split;
            theta = toRadians(point % 90);
            // Get the quadrant that we are in
            if (point > 269){
                quad = 1;
            } else if (point < 270 && point > 179){
                quad = 2;
            } else if (point < 180 && point > 89){
                quad = 3;
            } else {
                quad = 4;
            }
            // Determine the coordinates based on the quadrant
            switch (quad){
                case 1:
                    // sin(theta) = opp/hyp -> opp = hyp * sin(theta)
                    opp = (int) round(hyp * abs(sin(theta)));
                    // y-coordinate = center - opposite
                    y = centerY - opp;
                    
                    // a^2 + b^2 = c^2 -> sqrt(c^2-a^2) = b
                    adj = (int) round(sqrt((hyp*hyp) - (opp*opp)));
                    x = centerX + adj;
                    locs[z] = new Point(x, y);
                    break;
                case 2:
                    // sin(theta) = opp/hyp -> opp = hyp * sin(theta)
                    opp = (int) round(hyp * abs(sin(theta)));
                    // y-coordinate = center - opposite
                    x = centerX + opp;
                    
                    // a^2 + b^2 = c^2 -> sqrt(c^2-a^2) = b
                    adj = (int) round(sqrt((hyp*hyp) - (opp*opp)));
                    y = centerY + adj;
                    locs[z] = new Point(x, y);
                    break;
                case 3:
                    // sin(theta) = opp/hyp -> opp = hyp * sin(theta)
                    opp = (int) round(hyp * abs(sin(theta)));
                    // y-coordinate = center - opposite
                    y = centerY + opp;
                    
                    // a^2 + b^2 = c^2 -> sqrt(c^2-a^2) = b
                    adj = (int) round(sqrt((hyp*hyp) - (opp*opp)));
                    x = centerX - adj;
                    locs[z] = new Point(x, y);
                    break;
                case 4:
                    // sin(theta) = opp/hyp -> opp = hyp * sin(theta)
                    opp = (int) round(hyp * abs(sin(theta)));
                    // y-coordinate = center - opposite
                    x = centerX - opp;
                    
                    // a^2 + b^2 = c^2 -> sqrt(c^2-a^2) = b
                    adj = (int) round(sqrt((hyp*hyp) - (opp*opp)));
                    y = centerY - adj;
                    locs[z] = new Point(x, y);
                    break;
            }
        }
        return locs;
    }
                  
    private javax.swing.JPanel Frame;
    private javax.swing.JComboBox<String> algorithmCombo;
    private javax.swing.JLabel algorithmLabel;
    private javax.swing.JPanel animationPanel;
    private javax.swing.JPanel configPanel;
    private javax.swing.JComboBox<String> iterationCombo;
    private javax.swing.JLabel iterationsLabel;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLayeredPane layeredPane;
    private javax.swing.JLabel lifespanLabel;
    private javax.swing.JComboBox<String> lsCombo;
    private javax.swing.JPanel msgPanel;
    private javax.swing.ButtonGroup msgRadioGroup;
    private javax.swing.JRadioButton msgRadioOff;
    private javax.swing.JRadioButton msgRadioOn;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox<String> serversCombo;
    private javax.swing.JLabel serversLabel;
    private javax.swing.JRadioButton simRadioAuto;
    private javax.swing.ButtonGroup simRadioGroup;
    private javax.swing.JRadioButton simRadioManual;
    private javax.swing.JLabel simulationLabel;
    private javax.swing.JPanel srvCtlPanel;
    private javax.swing.JButton startButton;    
}
