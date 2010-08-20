package telluriumworks

import java.awt.Color
import org.jdesktop.swingx.painter.GlossPainter
import net.miginfocom.swing.MigLayout
import javax.swing.JTabbedPane

gloss = glossPainter(paint: new Color(1f, 1f, 1f, 0.2f),
    position: GlossPainter.GlossPosition.TOP)
stripes = pinstripePainter(paint: new Color(1f, 1f, 1f, 0.17f),
                           spacing: 5f)
matte = mattePainter(fillPaint: new Color(51, 51, 51))
compound = compoundPainter(painters: [matte, stripes, gloss])
actions {
   action(id: 'openAction',
      name: 'Open',
      mnemonic: 'O',
      accelerator: shortcut('O'),
      closure: controller.openFile)
   action(id: 'quitAction',
      name: 'Quit',
      mnemonic: 'Q',
      accelerator: shortcut('Q'),
      closure: controller.quit)
}

fileChooserWindow = fileChooser()
fileViewerWindow = application(title: 'TelluriumWorks',
  size:[480,320], locationByPlatform:true,
  iconImage: imageIcon('/tellurium.png').image,
  iconImages: [imageIcon('/tellurium.png').image]) {
  lookAndFeel('system')
  
   menuBar {
      menu('File') {
         menuItem openAction
         separator()
         menuItem quitAction
      }
   }

  borderLayout()
  //tabbedPane(id: 'tabGroup', selectedIndex: bind(value:model.tabSelected), constraints: CENTER)    //selectedIndex:bind{model.tabSelected},

  panel(layout: new MigLayout('fill')) {
    panel(layout: new MigLayout('fill'), border: titledBorder('Tellurium'), constraints: 'grow 100 1, wrap, height 70%') {
      hbox(constraints: "south") {

         tabbedPane(id: 'tabGroup', constraints: "grow 100 1, wrap")        //grow 100 1,

/*         panel(layout: new MigLayout('fill'), constraints: 'grow 50 , wrap' ) {
                label('Tellurium Script:', constraints: 'left')
                button ( id: 'runScript',
                  label: "Run",
                  actionPerformed: {this.scriptTxt.text=""},
                  constraints: "right,width 10px ,shrinkx,shrinky"
                )

         }*/
        vbox{
          panel(layout: new MigLayout('fill'), border: titledBorder('Tellurium Server'), constraints: 'grow, width 25%' ) {

              comboBox(id: 'serverType',
                  items: ["Local", "Remote"],
                  selectedIndex: 0,
                  constraints: "growx,width 20px"
              )

              label( id:'portlbl',
                  text: "Port:",
                  constraints: "shrinkx,height 25px,width 20px"
              )

              textField( id:'portValue',
                  constraints: "shrink,height 25px,width 50px"
              )

              checkBox(id: "multipleWindow", selected: false, text: "Multiple Windows", constraints:'wrap')

              hbox(constraints:'center'){
                button ( id: 'runBtn',
                    label: "Run",
                    actionPerformed: {controller.runSeleniumServer()},
                    constraints: "width 20px,shrinkx,wrap"
                )

                button ( id: 'stopBtn',
                    label: "Stop",
                    actionPerformed: {controller.stopSeleniumServer()},
                    constraints: "width 20px,shrinkx,wrap"
                )                
              }
          }

          panel(layout: new MigLayout('fill'), border: titledBorder('Tellurium Configuration'), constraints: 'grow, width 25%' ) {

              comboBox(id: 'browserType',
                  items: ["*chrome", "*firefox", "*iexplore", "*iehta"],
                  selectedIndex: 0,
                  constraints: "growx,width 20px"
              )                  

              label( id:'selServerlbl',
                  text: "Server Host:",
                  constraints: "shrinkx,height 25px,width 20px"
              )

              textField( id:'selServerValue',
                  constraints: "shrink,height 25px,width 50px"
              )

              label( id:'selPortlbl',
                  text: "Server Port:",
                  constraints: "shrinkx,height 25px,width 20px"
              )

              textField( id:'selPortValue',
                  constraints: "shrink,height 25px,width 50px"
              )

              button ( id: 'applyBtn',
                  label: "Apply",
                  actionPerformed: {controller.setTelluriumConfig()},
                  constraints: "width 20px ,shrinkx,wrap"
              )

          }
        }

/*        scrollPane(constraints: 'grow, width 50%, height 70%') {
            textArea(id: "scriptTxt",
                editable: true,
                lineWrap: true,
                wrapStyleWord: true)
        }*/

      }
    }

    panel(layout: new MigLayout('fill'), border: titledBorder('Console'), constraints: 'grow 100 1, wrap, height 30%') {
         button ( id: 'clearConsole',
                  label: "Clear",
                      actionPerformed: {this.consoleTxt.text=""},
                      constraints: "right,width 10px ,shrinkx,shrinky,wrap"
                )
        scrollPane(constraints: 'growx,width 100%,height 80%,span') {
             textArea(id: "consoleTxt",
                editable: false,
                lineWrap: true,
                wrapStyleWord: true)
        }
    }
  }
/*    jxheader(constraints: NORTH,
        title: "Tellurium Works",
        description: "Tellurium IDE to run Tellurium DSL script",
        titleForeground: Color.WHITE,
        descriptionForeground: Color.WHITE,
        icon: imageIcon("/tellurium.png"),
        preferredSize: [480,80],
        backgroundPainter: compound
    )
    jxtaskPaneContainer(constraints: CENTER) {
        jxtaskPane(title: "Task group 1") {
            jxlabel("Action 1")
        }
        jxtaskPane(title: "Task group 2", expanded: false) {
            label("Action 2")
        }
    } */


/*tabbedPane(tabPlacement: JTabbedPane.LEFT) {
  label('One', title:'One', tabToolTip:'Uno!')
  label('Green', title:'Green', tabBackground:java.awt.Color.GREEN)
//  label('Stop Operation', title:'Stop Operation', tabMnemonic:'O')
//  label('Stop Operation', title:'Stop Operation', tabDisplayedMnemonicIndex:5)
  panel(name:'One') {
        label('One')
  }
  panel(name:'Two') {
        label('Two')
  }

}*/
}
