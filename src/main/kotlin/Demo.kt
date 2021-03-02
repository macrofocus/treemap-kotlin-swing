import com.treemap.*
import com.treemap.RenderingFactory.Companion.FLAT
import com.treemap.RenderingFactory.Companion.getInstance
import com.treemap.TreeMap.Companion.setLicenseKey
import org.mkui.colormap.MutableColorMap
import org.mkui.font.CPFont
import org.mkui.labeling.EnhancedLabel
import org.mkui.palette.FixedPalette
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
import java.nio.charset.Charset
import javax.swing.*

object Demo {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        setLicenseKey("My Company", "ABC12-ABC12-ABC12-ABC12-ABC12-ABC12")

        val json = String(Demo::class.java.getResourceAsStream("Forbes Global 2000 - 2020.json").readAllBytes(), Charset.forName("UTF-8"))
        val dataFrame: DataFrame<Int, String, Any?> = JsonDataFrame(json)
        val treeMap: AbstractTreeMap<Int, String> = DefaultTreeMap(dataFrame)
        val model = treeMap.model
        val settings = model!!.settings

        // General
        settings.setRendering(FLAT)

        // Group by
        settings.setGroupByByNames("Sector", "Industry")

        // Size
        settings.setSizeByName("Market Value")

        // Color
        settings.setColorByName("Profits")
        val profitsSettings = settings.getColumnSettings("Profits")
        val negpos = PaletteFactory()["negpos"]!!.getPalette()
        val colorMap = model.getColorMap("Profits")
        colorMap!!.palette = negpos
        colorMap.interval!!.setValue(-88.205, 176.41)

        // Label
        val companySettings = settings.getColumnSettings("Company")
        companySettings.setLabelingFont(CPFont(Font("Helvetica", Font.PLAIN, 9))) // 9 points is the minimum size that will be displayed
        companySettings.setLabelingMinimumCharactersToDisplay(5)
        companySettings.setLabelingResizeTextToFitShape(true)
        companySettings.setLabelingVerticalAlignment(EnhancedLabel.CENTER)
        companySettings.setLabelingHorizontalAlignment(EnhancedLabel.CENTER)
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

    private fun createRenderingComboBox(settings: TreeMapSettings<String>): JComboBox<Rendering?> {
        val renderingComboBox: JComboBox<Rendering?> =
            object : JComboBox<Rendering?>(SingleSelectionComboBoxModel(settings.getRenderingSelection(), getInstance().getRenderings())) {
                override fun getMaximumSize(): Dimension {
                    return super.getPreferredSize()
                }
            }
        renderingComboBox.border = BorderFactory.createTitledBorder("Rendering")
        renderingComboBox.alignmentX = 0f
        return renderingComboBox
    }
}
