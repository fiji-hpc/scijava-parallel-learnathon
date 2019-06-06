package cz.it4i.parallel.learnathon;

import static cz.it4i.parallel.learnathon.Constants.LEARNATHON_DEMO_PROFILE_NAME;

import org.scijava.Context;
import org.scijava.command.Command;
import org.scijava.parallel.ParallelService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import cz.it4i.parallel.runners.HPCSettings;

@Plugin(type = Command.class, headless = true)
public class InitDemoProfile implements Command {

	@Parameter
	private ParallelService service;

	@Parameter
	private Context context;

	@Override
	public void run() {
		HPCSettings settings = HPCSettingsGui.showDialog(context);
		HPCProfile hpcProfile = new HPCProfile(HPCFSTParallizationParadigm.class,
			LEARNATHON_DEMO_PROFILE_NAME, settings);
		service.addProfile(hpcProfile);
	}
}
