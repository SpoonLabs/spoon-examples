package fr.inria.gforge.spoon.transformation.retry.template;

import spoon.reflect.code.CtBlock;
import spoon.template.BlockTemplate;
import spoon.template.Parameter;

/**
 * Created by nicolas on 22/01/2015.
 */
public class RetryTemplate extends BlockTemplate {
	public RetryTemplate(CtBlock _original_, int _attempts_, long _delay_, boolean _verbose_) {
		this._original_ = _original_;
		this._attempts_ = _attempts_;
		this._delay_ = _delay_;
		this._verbose_ = _verbose_;
	}

	@Parameter
	CtBlock _original_;

	@Parameter
	int _attempts_;

	@Parameter
	long _delay_;

	@Parameter
	boolean _verbose_;

	@Override
	public void block() {
		int attempt = 0;
		Throwable lastTh = null;
		while (attempt++ < _attempts_) {
			try {
				_original_.S();
			} catch (Throwable ex) {
				lastTh = ex;
				if (_verbose_) {
					ex.printStackTrace();
				}
				try {
					Thread.sleep(_delay_);
				} catch (InterruptedException ex2) {
				}
			}
		}
		if (lastTh != null) {
			throw new RuntimeException(lastTh);
		}
	}
}
