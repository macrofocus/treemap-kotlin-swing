import com.treemap.*
import com.treemap.AlgorithmFactory.Companion.getInstance
import com.treemap.RenderingFactory.Companion.FLAT
import org.mkui.font.crossplatform.CPFont
import org.mkui.labeling.EnhancedLabel
import org.mkui.palette.PaletteFactory
import org.mkui.swing.HierarchicalComboBox
import org.mkui.swing.Orientation
import org.mkui.swing.SingleSelectionComboBoxModel
import org.molap.dataframe.DataFrame
import org.molap.dataframe.JsonDataFrame
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import java.io.IOException
import javax.swing.*

object Demo {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
//        setLicenseKey("My Company", "ABC12-ABC12-ABC12-ABC12-ABC12-ABC12")

        val inputStream = Demo::class.java.getResourceAsStream("Forbes Global 2000 - 2021.json")
        val dataFrame: DataFrame<Int, String, Any?> = JsonDataFrame.fromInputStream(inputStream)
        val treeMap: AbstractTreeMap<Int, String> = DefaultTreeMap(dataFrame)
        val model = treeMap.model
        val settings = model!!.settings

        // General
        settings.rendering = FLAT

        // Group by
        settings.groupByColumns = listOf("Sector", "Industry")

        // Size
        settings.sizeColumn = "Market Value"

        // Color
        settings.colorColumn = "Profits"
        val profitsSettings = settings.getColumnSettings("Profits")
        val negpos = PaletteFactory.instance["negpos"]!!.getPalette()
        val colorMap = model.getColorMap("Profits")
        colorMap!!.palette = negpos
        colorMap.interval!!.setValue(-63.93, 127.86)

        // Label
        val companySettings = settings.getColumnSettings("Company")
        companySettings.setLabelingFont(CPFont(Font("Helvetica", Font.PLAIN, 9)).nativeFont) // 9 points is the minimum size that will be displayed
        companySettings.setLabelingMinimumCharactersToDisplay(5)
        companySettings.setLabelingResizeTextToFitShape(true)
        companySettings.setLabelingVerticalAlignment(EnhancedLabel.CENTER)
        companySettings.setLabelingHorizontalAlignment(EnhancedLabel.CENTER)

        treeMap.view!!.isShowTiming = false

        val configuration = createConfiguration(model, settings)
        val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, configuration, treeMap.component.nativeComponent)
        val mainPanel = JPanel(BorderLayout())
        mainPanel.add(createGroupBy(Orientation.Horizontal, model, settings), BorderLayout.NORTH)
        mainPanel.add(splitPane)
        val frame = JFrame("TreeMap")
        frame.contentPane.add(mainPanel)
        frame.setSize(1024, 768)
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }

    private fun createConfiguration(model: TreeMapModel<AbstractTreeMapNode<Int, String>, Int, String>?, settings: TreeMapSettings<String>): JPanel {
        val configuration = JPanel()
        configuration.layout = BoxLayout(configuration, BoxLayout.PAGE_AXIS)
        configuration.add(createGroupBy(Orientation.Vertical, model, settings))
        configuration.add(createSizeComboBox(model, settings))
        configuration.add(createColorComboBox(model, settings))
        configuration.add(createAlgorithmComboBox(settings.getDefaultColumnSettings()))
        configuration.add(createRenderingComboBox(settings))
        configuration.add(Box.createGlue())
        return configuration
    }

    private fun createGroupBy(
        orientation: Orientation,
        model: TreeMapModel<AbstractTreeMapNode<Int, String>, Int, String>?,
        settings: TreeMapSettings<String>
    ): HierarchicalComboBox<String> {
        val groupBy: HierarchicalComboBox<String> =
            object : HierarchicalComboBox<String>(orientation, settings.getGroupByFieldsSelection(), model!!.groupByTreeMapColumns) {
                override fun getMaximumSize(): Dimension {
                    return super.getPreferredSize()
                }
            }
        groupBy.border = BorderFactory.createTitledBorder("Group by")
        groupBy.alignmentX = 0f
        return groupBy
    }

    private fun createSizeComboBox(
        model: TreeMapModel<AbstractTreeMapNode<Int, String>, Int, String>?,
        settings: TreeMapSettings<String>
    ): JComboBox<String?> {
        val sizeComboBox: JComboBox<String?> =
            object : JComboBox<String?>(SingleSelectionComboBoxModel(settings.getSizeFieldSelection(), model!!.sizeTreeMapColumns)) {
                override fun getMaximumSize(): Dimension {
                    return super.getPreferredSize()
                }
            }
        sizeComboBox.border = BorderFactory.createTitledBorder("Size")
        sizeComboBox.alignmentX = 0f
        return sizeComboBox
    }

    private fun createColorComboBox(
        model: TreeMapModel<AbstractTreeMapNode<Int, String>, Int, String>?,
        settings: TreeMapSettings<String>
    ): JComboBox<String?> {
        val sizeComboBox: JComboBox<String?> =
            object : JComboBox<String?>(SingleSelectionComboBoxModel(settings.getColorColumnSelection(), model!!.colorTreeMapColumns)) {
                override fun getMaximumSize(): Dimension {
                    return super.getPreferredSize()
                }
            }
        sizeComboBox.border = BorderFactory.createTitledBorder("Size")
        sizeComboBox.alignmentX = 0f
        return sizeComboBox
    }

    private fun createAlgorithmComboBox(settings: TreeMapColumnSettings): JComboBox<Algorithm> {
        val algorithmComboBox: JComboBox<Algorithm> =
            object : JComboBox<Algorithm>(SingleSelectionComboBoxModel(settings.getAlgorithmProperty(), getInstance().getAlgorithms())) {
                override fun getMaximumSize(): Dimension {
                    return super.getPreferredSize()
                }
            }
        algorithmComboBox.border = BorderFactory.createTitledBorder("Algorithm")
        algorithmComboBox.alignmentX = 0f
        return algorithmComboBox
    }

    private fun createRenderingComboBox(settings: TreeMapSettings<String>): JComboBox<Rendering?> {
        val renderingComboBox: JComboBox<Rendering?> =
            object : JComboBox<Rendering?>(SingleSelectionComboBoxModel(settings.renderingSelection, RenderingFactory.instance.getRenderings())) {
                override fun getMaximumSize(): Dimension {
                    return super.getPreferredSize()
                }
            }
        renderingComboBox.border = BorderFactory.createTitledBorder("Rendering")
        renderingComboBox.alignmentX = 0f
        return renderingComboBox
    }
}
