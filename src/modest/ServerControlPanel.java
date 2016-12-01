package modest;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * Creates a control panel for a server with 2 sliders and 4 buttons
 * 
 */
public class ServerControlPanel extends JPanel{
    
    JLayeredPane jlp;
    ServerLabel server;
    int myId;
    
    public ServerControlPanel(JLayeredPane jlp, ServerLabel server){
        super();
        this.server = server;
        this.myId = server.id;
        this.jlp = jlp;
        initComponents();
    }
    
    @SuppressWarnings("unchecked")                       
    private void initComponents() {
        
        // Initialize the components of the panel
        JLabel nameLabel = new javax.swing.JLabel();
        nameLabel.setText("<html><font color='black',size=4>SERVER " + myId + 
                          "</font></html>");
        
        JLabel msgLabel = new javax.swing.JLabel();
        msgLabel.setText("<html><font color='black',size=4>Msg Delay" +
                         "</font></html>");
        
        JLabel msgStartLabel = new javax.swing.JLabel();
        msgStartLabel.setText("<html><font color='black',size=2>0" +
                         "</font></html>");
        
        JLabel msgEndLabel = new javax.swing.JLabel();
        msgEndLabel.setText("<html><font color='black',size=2>10" +
                         "</font></html>");
        
        JLabel critLabel = new javax.swing.JLabel();
        critLabel.setText("<html><font color='black',size=4>CS Time" +
                          "</font></html>");
        
        JLabel critStartLabel = new javax.swing.JLabel();
        critStartLabel.setText("<html><font color='black',size=2>1" +
                         "</font></html>");
        
        JLabel critEndLabel = new javax.swing.JLabel();
        critEndLabel.setText("<html><font color='black',size=2>10" +
                         "</font></html>");
        
        JButton reqBtn = new javax.swing.JButton();
        reqBtn.setBackground(new java.awt.Color(153, 153, 153));
        reqBtn.setText("Req");
        reqBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reqBtnActionPerformed(evt);
            }
        });
        
        JButton relBtn = new javax.swing.JButton();
        relBtn.setBackground(new java.awt.Color(153, 153, 153));
        relBtn.setText("Rel");
        relBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relBtnActionPerformed(evt);
            }
        });
        
        if (Modest.automated){
            reqBtn.setEnabled(false);
            relBtn.setEnabled(false);
        }
        
        JButton dropBtn = new javax.swing.JButton();
        dropBtn.setBackground(new java.awt.Color(153, 153, 153));
        dropBtn.setText("Drop");
        dropBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dropBtnActionPerformed(evt);
            }
        });
        
        JButton killBtn = new javax.swing.JButton();
        killBtn.setBackground(new java.awt.Color(153, 153, 153));
        killBtn.setText("KILL");
        killBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                killBtnActionPerformed(evt);
            }
        });
        
        JSlider msgSlider = new javax.swing.JSlider(JSlider.HORIZONTAL,0,10,0);
        msgSlider.setBackground(new java.awt.Color(102, 102, 102));
        msgSlider.setMajorTickSpacing(10);
        msgSlider.setMinorTickSpacing(1);
        msgSlider.setPaintTicks(true);
        msgSlider.addChangeListener(new SlideListener("msg"));
        
        JSlider critSlider = new javax.swing.JSlider(JSlider.HORIZONTAL,1,10,1);
        critSlider.setBackground(new java.awt.Color(102, 102, 102));
        critSlider.setMajorTickSpacing(9);
        critSlider.setMinorTickSpacing(1);
        critSlider.setPaintTicks(true);
        critSlider.addChangeListener(new SlideListener("cs"));
        
        this.setBackground(new java.awt.Color(102, 102, 102));
        this.setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(255, 153, 51)));
        javax.swing.GroupLayout jPanelLayout = 
                new javax.swing.GroupLayout(this);
        this.setLayout(jPanelLayout);
        
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(nameLabel))
            .addGroup(jPanelLayout.createSequentialGroup()
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addComponent(critLabel)
                        .addGap(20, 20, 20)
                        .addComponent(critStartLabel)
                        //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(critSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addComponent(msgLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(msgStartLabel)
                        //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(msgSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(msgEndLabel)
                    .addComponent(critEndLabel)))
            .addGroup(jPanelLayout.createSequentialGroup()
                .addComponent(reqBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(relBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dropBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(killBtn))
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addComponent(nameLabel)
                //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(msgLabel)
                        .addComponent(msgSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(msgEndLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(msgStartLabel))
                //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(critLabel)
                        .addComponent(critStartLabel))
                    .addComponent(critSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(critEndLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reqBtn)
                    .addComponent(relBtn)
                    .addComponent(dropBtn)
                    .addComponent(killBtn)))
        );
    }
    
    private void reqBtnActionPerformed(java.awt.event.ActionEvent evt) {
        Modest.srvLinkers.get(server.id).req = true;
    }
    
    private void relBtnActionPerformed(java.awt.event.ActionEvent evt) {
        Modest.srvLinkers.get(server.id).rel = true;
    }
    
    private void dropBtnActionPerformed(java.awt.event.ActionEvent evt) {
        Modest.srvLinkers.get(server.id).dropMsg = true;
    }
    
    private void killBtnActionPerformed(java.awt.event.ActionEvent evt) {
        // Display a skull over the killed server and clean up
        try{
            String img_name = "skull.png";
            ImageIcon icon = new ImageIcon(getClass().getResource(img_name));
            Point p = new Point(server.x, server.y);
            jlp.add(new KillLabel(icon, p), 0, 0);
            
            // Close the connections for the server
            Connector conn = Modest.Connectors.get(myId);
            conn.closeSockets();
            String msg = "KILLED server " + myId;
            Modest.mqueue.enqueue(msg);
            System.out.println(msg);
        } catch (Exception exc){
            System.err.println(exc);
            exc.printStackTrace();
        }
    }
    
    class SlideListener implements ChangeListener {
        
        String type;
        
        public SlideListener(String type){
            this.type = type;
        }
        
        @Override
        public void stateChanged(ChangeEvent e) {
            // Override to be able to set delays for the server
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                int delay = (int)source.getValue();
                if (type.equals("msg")){
                    Modest.srvLinkers.get(myId).delay = delay;
                    System.out.println("Server " + myId + " Message delay "
                                       + "changed to " + delay + " seconds");
                } else if (type.equals("cs")){
                    Modest.srvLinkers.get(myId).csDelay = delay;
                    System.out.println("Server " + myId + " CS occupancy time "
                                       + "changed to " + delay + " seconds");
                }
            }    
        }
    }
}
