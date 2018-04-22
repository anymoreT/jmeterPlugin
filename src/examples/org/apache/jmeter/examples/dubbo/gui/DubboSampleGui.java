
package org.apache.jmeter.examples.dubbo.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.protocol.jms.sampler.PublisherSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;


public class DubboSampleGui extends AbstractSamplerGui {

    private static final Logger log = LoggingManager.getLoggerForClass();

    /**
     */
    private static final long serialVersionUID = -3248204995359935007L;

    private JComboBox<String> protocolText;
    private JComboBox<String> paramTypeText;
    private JTextField addressText;
    private JTextField timeoutText;
    private JTextField versionText;
    private JTextField retriesText;
    private JTextField clusterText;
    private JTextField interfaceText;
    private JTextField methodText;
    private DefaultTableModel model;
    private String[] columnNames = {"paramName","paramType", "paramValue"};
    private Vector rowVector = new Vector();

    private Object[] tmpRow = {"", paramTypeText,""};

    public DubboSampleGui() {
        super();
        init();
    }

    /**
     * 初始化界面布局与元素
     *
     * @author ningyu
     * @date 2018年2月8日 上午10:46:06
     *
     */
    private void init() {
        //所有设置panel，垂直布局
        JPanel settingPanel = new VerticalPanel(5, 0);
        settingPanel.setBorder(makeBorder());
        Container container = makeTitlePanel();

        JLabel linklabel = new JLabel("<html><a href='https://github.com/ningyu1/jmeter-plugins-dubbo'>The plug-in author is Ningyu, Plug-in GitHub</a></html>");
        //   linklabel.setIcon(new ImageIcon(getClass().getResource("/images/about.png")));
        linklabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URL("https://github.com/ningyu1/jmeter-plugins-dubbo").toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        container.add(linklabel);
        settingPanel.add(container);

        JPanel serverSettings = new VerticalPanel();
        serverSettings.setBorder(BorderFactory.createTitledBorder("Server Settings"));

        //Protocol
        JPanel ph = new HorizontalPanel();
        JLabel protocolLable = new JLabel("Protocol:", SwingConstants.RIGHT);
        protocolText = new JComboBox<String>(new String[]{"dubbo@直连", "zookeeper@注册中心", "multicast@注册中心", "redis@注册中心", "simple@注册中心"});
        paramTypeText = new JComboBox<String>(new String[]{"int", "string", "boolean", "float", "dubble"});
        protocolLable.setLabelFor(protocolText);
        ph.add(protocolLable);
        ph.add(protocolText);
        serverSettings.add(ph);

        //Address
        JPanel ah = new HorizontalPanel();
        JLabel addressLable = new JLabel("Address:", SwingConstants.RIGHT);
        addressText = new JTextField(10);
        addressLable.setLabelFor(addressText);
        ah.add(addressLable);
        ah.add(addressText);
        serverSettings.add(ah);

        JPanel h = new HorizontalPanel();
        //Timeout
        JLabel timeoutLable = new JLabel(" Timeout:", SwingConstants.RIGHT);
        timeoutText = new JTextField(10);
        timeoutText.setText("1200000");
        timeoutLable.setLabelFor(timeoutText);
        h.add(timeoutLable);
        h.add(timeoutText);
        //Version
        JLabel versionLable = new JLabel("Version:", SwingConstants.RIGHT);
        versionText = new JTextField(10);
        versionText.setText("1.0.0");
        versionLable.setLabelFor(versionText);
        h.add(versionLable);
        h.add(versionText);
        //Retries
        JLabel retriesLable = new JLabel("Retries:", SwingConstants.RIGHT);
        retriesText = new JTextField(10);
        retriesText.setText("0");
        retriesLable.setLabelFor(retriesText);
        h.add(retriesLable);
        h.add(retriesText);
        //Cluster
        JLabel clusterLable = new JLabel("Cluster:", SwingConstants.RIGHT);
        clusterText = new JTextField(10);
        clusterText.setText("failfast");
        clusterLable.setLabelFor(clusterText);
        h.add(clusterLable);
        h.add(clusterText);
        serverSettings.add(h);

        JPanel interfaceSettings = new VerticalPanel();
        interfaceSettings.setBorder(BorderFactory.createTitledBorder("Interface Settings"));
        //Interface
        JPanel ih = new HorizontalPanel();
        JLabel interfaceLable = new JLabel("Interface:", SwingConstants.RIGHT);
        interfaceText = new JTextField(10);
        interfaceLable.setLabelFor(interfaceText);
        ih.add(interfaceLable);
        ih.add(interfaceText);
        interfaceSettings.add(ih);
        //Method
        JPanel mh = new HorizontalPanel();
        JLabel methodLable = new JLabel("   Method:", SwingConstants.RIGHT);
        methodText = new JTextField(10);
        methodLable.setLabelFor(methodText);
        mh.add(methodLable);
        mh.add(methodText);
        interfaceSettings.add(mh);

        //表格panel
        JPanel tablePanel = new HorizontalPanel();


        //Args
        JLabel argsLable = new JLabel("        Args:", SwingConstants.RIGHT);
        model = new DefaultTableModel();
        DefaultListSelectionModel listModel = new DefaultListSelectionModel();
        TableColumnModel columnModel = new DefaultTableColumnModel();
//        model.setDataVector(new String[][]{{"", ""}}, columnNames);
        model.setDataVector(null, columnNames);
     //   final JTable table = new JTable(model);
        TableColumn fistColumn = new TableColumn();

        columnModel.addColumn(fistColumn);
        final JTable table = new JTable(model,columnModel,listModel);
        table.setRowHeight(40);
        JTextArea jTextArea = new JTextArea();



        //添加按钮
//        JButton addBtn = new JButton("增加");
//        addBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent arg0) {
//                // TODO Auto-generated method stub
//                rowVector.add("");
//                rowVector.add(paramTypeText);
//                rowVector.add("1");
//               // model.addRow(tmpRow);
//                model.addRow(rowVector);
//
//            }
//        });
//        JButton delBtn = new JButton("删除");
//        delBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent arg0) {
//                // TODO Auto-generated method stub
//                int rowIndex = table.getSelectedRow();
//                if(rowIndex != -1)
//                    model.removeRow(rowIndex);
//            }
//        });
        //表格滚动条
        JScrollPane scrollpane = new JScrollPane(table);
        tablePanel.add(argsLable);
        tablePanel.add(scrollpane);
        tablePanel.add(jTextArea);
//        tablePanel.add(addBtn);
//        tablePanel.add(delBtn);
        interfaceSettings.add(tablePanel);

        //所有设置panel
        settingPanel.add(serverSettings);
        settingPanel.add(interfaceSettings);

        //全局布局设置
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(settingPanel,BorderLayout.CENTER);
    }

    /**
     * 这个方法将Smapler的数据设置到gui中
     */
    @Override
    public void configure(TestElement element) {
        super.configure(element);
        log.info("sample赋值给gui");
        // DubboSample sample = (DubboSample) element;
        protocolText.setSelectedItem("version");
        addressText.setText("127.0.0.1");
        versionText.setText("123");
        timeoutText.setText("1000");
        retriesText.setText("1");
        clusterText.setText("1");
        interfaceText.setText("interface.get");
        methodText.setText("register");
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("参数名");
        columnNames.add("参数类型");
        columnNames.add("参数值");
        Vector<String> methodVo = new Vector<String>();
        methodVo.add("1234");
        methodVo.add("456");
     //   model.setDataVector(methodVo, columnNames);
    }

    /**
     * 创建新的sampler。并且将它传给你创建的modifyTestElement(TestElement)方法。
     */
    @Override
    public TestElement createTestElement() {
        log.info("创建sample对象");
        PublisherSampler sampler = new PublisherSampler();
        //  setupSamplerProperties(sampler);

        return sampler;
    }

    /**
     * 这个方法应该返回代表的component的title/name的名字
     */
    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    /**
     * 这个方法是用来将数据从你的gui传到TestElement.这个方法和configure()方法在逻辑上是相反的
     */
    @SuppressWarnings("unchecked")
    @Override
    public void modifyTestElement(TestElement element) {
        log.info("gui数据赋值给sample");
        //给sample赋值

    }
//
//    private Vector<Vector<String>> paserMethodArgsData(List<MethodArgument> list) {
//    	Vector<Vector<String>> res = new Vector<Vector<String>>();
//    	for (MethodArgument args : list) {
//    		Vector<String> v = new Vector<String>();
//    		v.add(args.getParamType());
//    		v.add(args.getParamValue());
//    		res.add(v);
//    	}
//    	return res;
//    }
//
//    private List<MethodArgument> getMethodArgsData(Vector<Vector<String>> data) {
//    	List<MethodArgument> params = new ArrayList<MethodArgument>();
//    	if (!data.isEmpty()) {
//    		 //处理参数
//            Iterator<Vector<String>> it = data.iterator();
//            while(it.hasNext()) {
//                Vector<String> param = it.next();
//                if (!param.isEmpty()) {
//                	params.add(new MethodArgument(param.get(0), param.get(1)));
//                }
//            }
//    	}
//    	return params;
//    }

    /**
     * 这个方法应该返回代表的Sample的name
     */
    @Override
    public String getStaticLabel() {
        return "Dubbo 请求发送器";
    }

    /**
     * 这个方法是用来在你创建新的sampler时，清除数据的。
     */
    @Override
    public void clearGui() {
        log.info("清空gui数据");
        super.clearGui();
        protocolText.setSelectedIndex(0);
        addressText.setText("");
        timeoutText.setText("1200000");
        versionText.setText("1.0.0");
        interfaceText.setText("");
        methodText.setText("");
//        model.setDataVector(new String[][]{{"", ""}}, columnNames);
        model.setDataVector(null, columnNames);
    }

}


