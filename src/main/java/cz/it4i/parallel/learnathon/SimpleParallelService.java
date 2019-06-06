// TODO: Add copyright stuff

package cz.it4i.parallel.learnathon;

import static cz.it4i.parallel.learnathon.Constants.LEARNATHON_DEMO_PROFILE_NAME;

import java.util.Optional;

import org.scijava.Priority;
import org.scijava.command.CommandService;
import org.scijava.parallel.DefaultParallelService;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

// TODO: Add description

@Plugin(type = Service.class, priority = Priority.HIGH)
public class SimpleParallelService extends
	DefaultParallelService
{

	@Parameter
	private CommandService commandService;


	public void initDemoProfile() {
		commandService.run(InitDemoProfile.class, false);
	}

	@Override
	public void selectProfile(String name) {

		super.selectProfile(name);
		if (name.equals(LEARNATHON_DEMO_PROFILE_NAME) && getParadigm() == null) {
			initDemoProfile();
			super.selectProfile(name);
		}

	}

	@Override
	public ParallelizationParadigm getParadigm() {
		ParallelizationParadigm result = super.getParadigm();
		Optional<HPCProfile> profile = getProfiles().stream().filter(
			p -> p instanceof HPCProfile).map(p -> (HPCProfile) p).filter(p -> p
				.getName().equals(LEARNATHON_DEMO_PROFILE_NAME)).findAny();
		if (profile.isPresent() && result instanceof HPCFSTParallizationParadigm) {
			((HPCFSTParallizationParadigm) result).setSettings(profile.get()
				.getHpcSettings());
		}
		return result;
	}

}
