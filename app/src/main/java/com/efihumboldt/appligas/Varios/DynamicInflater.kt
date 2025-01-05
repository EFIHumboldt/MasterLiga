import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.Xml
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader

class DynamicInflater(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    fun inflateLayout(layoutResId: Int) {
        LayoutInflater.from(context).inflate(layoutResId, this, true)
    }
    fun inflateXml(xmlData: String?) {
        xmlData?.let {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(StringReader(xmlData))

            // Convertir el XML en un LayoutRes
            val layoutResId = convertXmlToLayoutRes(parser)

            // Inflar la vista con el LayoutRes
            if (layoutResId != 0) {
                LayoutInflater.from(context).inflate(layoutResId, this, true)
            }
        }
    }

    private fun convertXmlToLayoutRes(parser: XmlPullParser): Int {
        Log.d("CrucesFragment", "Inside convertXmlToLayoutRes")

        var eventType = parser.eventType
        var layoutResId = 0

        while (eventType != XmlPullParser.END_DOCUMENT) {
            Log.d("CrucesFragment", "Inside while loop")

            when (eventType) {
                XmlPullParser.START_TAG -> {
                    val tagName = parser.name
                    Log.d("CrucesFragment", "Tag name: $tagName")

                    if (tagName == "layout") {
                        val layoutAttribute = parser.getAttributeValue(null, "layout")
                        Log.d("CrucesFragment", "Layout attribute: $layoutAttribute")

                        if (layoutAttribute != null) {
                            layoutResId = resources.getIdentifier(layoutAttribute, "layout", context.packageName)
                        }
                    }
                }
            }
            eventType = parser.next()
        }

        return layoutResId
    }
}
