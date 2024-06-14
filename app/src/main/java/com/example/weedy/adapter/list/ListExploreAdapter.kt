import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weedy.R
import com.example.weedy.data.entities.LocalGenetic
import com.example.weedy.databinding.ListItemExploreBinding

class ListExploreAdapter(private val dataset: List<LocalGenetic>) :
    RecyclerView.Adapter<ListExploreAdapter.ListItemViewHolder>() {

    private val TAG = "ListExploreAdapter"

    inner class ListItemViewHolder(val binding: ListItemExploreBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding =
            ListItemExploreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {

        val listEntry = dataset[position]

        with(holder.binding) {
            if (listEntry.strainImageURL == "") {
                exploreItemIV.setImageResource(R.drawable.example_plant)
            } else {
                exploreItemIV.load(
                    listEntry.strainImageURL
                )
            }
            exploreItemStrainTV.text = listEntry.strainName
            exploreItemTypeTV.text = listEntry.strainType
            exploreItemThcTV.text = "${listEntry.thcLevel} THC"
            exploreItemTerpeneTV.text = listEntry.mostCommonTerpene
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}