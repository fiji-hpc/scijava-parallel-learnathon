package cz.it4i.parallel.learnathon;

import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.ParallelizationParadigmProfile;

import cz.it4i.parallel.runners.HPCSettings;
import lombok.Getter;

class HPCProfile extends ParallelizationParadigmProfile {

	@Getter
	private final HPCSettings hpcSettings;

	@Getter
	private String name;

	public HPCProfile(Class<? extends ParallelizationParadigm> paradigmType,
		String profileName, HPCSettings setting)
	{
		super(paradigmType, profileName);
		this.hpcSettings = setting;
		this.name = profileName;
	}

}
