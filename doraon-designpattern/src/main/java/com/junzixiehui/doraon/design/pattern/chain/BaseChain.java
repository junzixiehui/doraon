package com.junzixiehui.doraon.design.pattern.chain;

import java.util.Collection;
import java.util.Iterator;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/12/1  16:15
 * @version: 1.0
 */
public class BaseChain implements Chain {
	protected Command[] commands = new Command[0];

	public BaseChain() {
	}

	public BaseChain(Command command) {
		this.addCommand(command);
	}

	public BaseChain(Command[] commands) {
		if (commands == null) {
			throw new IllegalArgumentException();
		} else {
			for(int i = 0; i < commands.length; ++i) {
				this.addCommand(commands[i]);
			}

		}
	}

	public BaseChain(Collection commands) {
		if (commands == null) {
			throw new IllegalArgumentException();
		} else {
			Iterator elements = commands.iterator();

			while(elements.hasNext()) {
				this.addCommand((Command)elements.next());
			}

		}
	}

	@Override
	public void addCommand(Command command) {
		if (command == null) {
			throw new IllegalArgumentException();
		} else {
			Command[] results = new Command[this.commands.length + 1];
			System.arraycopy(this.commands, 0, results, 0, this.commands.length);
			results[this.commands.length] = command;
			this.commands = results;
		}
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (context == null) {
			throw new IllegalArgumentException();
		} else {
			boolean saveResult = false;
			Exception saveException = null;
			int n = this.commands.length;

			for(int i = 0; i < n; ++i) {
				try {
					saveResult = this.commands[i].execute(context);
					if (saveResult) {
						break;
					}
				} catch (Exception var7) {
					saveException = var7;
					break;
				}
			}

			if (saveException != null) {
				throw saveException;
			} else {
				return saveResult;
			}
		}
	}

	Command[] getCommands() {
		return this.commands;
	}
}