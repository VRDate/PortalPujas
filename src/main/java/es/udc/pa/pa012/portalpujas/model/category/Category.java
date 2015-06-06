package es.udc.pa.pa012.portalpujas.model.category;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@Immutable
public class Category {

	private Long categoryId;
	private String name;

	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}

	@SequenceGenerator( // It only takes effect for
			name = "CategoryIdGenerator", // databases providing identifier
			sequenceName = "CategorySeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,
			generator = "CategoryIdGenerator")
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", name=" + name + "]";
	}

}
