package cz.it4i.parallel.learnathon;

import org.scijava.command.Command;
import org.scijava.command.CommandService;
import org.scijava.parallel.ParallelService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class,
	menuPath = "Plugins > SciJava Parallel > Set profile for Learnathon 2019")
public class SetDemoProfile implements Command {

	@Parameter
	ParallelService service;

	@Parameter
	CommandService commandService;

	@Override
	public void run() {
		service.deleteProfiles();
		commandService.run(InitDemoProfile.class, false);
	}
}
