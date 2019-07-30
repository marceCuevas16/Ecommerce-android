package com.marcecuevas.easybuy.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.marcecuevas.easybuy.R
import com.marcecuevas.easybuy.data.model.DTO.MainImageDTO
import com.marcecuevas.easybuy.view.component.OverlayImageViewer
import com.stfalcon.imageviewer.StfalconImageViewer

class SliderViewPager(private val context : Context, val images: List<MainImageDTO>?) : PagerAdapter(),
    OverlayImageViewer.OverlayListener {

    private var layoutInflater : LayoutInflater? = null

    var viewer: StfalconImageViewer<MainImageDTO?>? = null
    var overlayView: OverlayImageViewer? = null

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view ===  `object`
    }

    override fun getCount(): Int {
        images?.let { return it.size }
        return 0
    }

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater!!.inflate(R.layout.item_slider_viewpager , null)
        val image = v.findViewById<View>(R.id.imageview) as ImageView

        //overlayView = OverlayImageViewer(context)

        setupListener(image,position)

        images?.get(position)?.let {
            Glide.with(context)
                .load("http:${it.url}")
                .into(image)
        }

        val vp = container as ViewPager
        vp.addView(v , 0)

        return v
    }

    fun setupListener(toImage: ImageView, position: Int){
        toImage.setOnClickListener(){
            viewer = StfalconImageViewer.Builder<MainImageDTO?>(context, images) { view, image ->
                Glide.with(context)
                    .load("http:${image?.url}")
                    .into(view)
            }
            //.withOverlayView(overlayView)
            .withStartPosition(position)
//            .withImageChangeListener() { position ->
//                images?.size?.let { it1 -> overlayView?.setup(position + 1, it1) }
//            }
            .show()
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View
        vp.removeView(v)
    }

    override fun onCloseClicked() {
        viewer?.dismiss()
        overlayView?.removeAllViews()
    }

}